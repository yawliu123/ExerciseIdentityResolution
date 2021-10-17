package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByAddressGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByCompanyNumberGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.CompanyXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Linear_Authorities_gleif {
    private static final Logger logger = WinterLogManager.activateLogger("default");


    public static void main(String[] args) throws Exception{
    //loading data
        System.out.println("*\n*\tLoading datasets\n*");

        HashedDataSet<Company,Attribute> data_authorities = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_authorities_gleif.xml"),"/companies/company",data_authorities);


        HashedDataSet<Company,Attribute> data_gleif = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_gleif_authorities.xml"),"/companies/company",data_gleif);

        /**
         * Gleif and Authorities
         */

        System.out.println("*\n*\tLoading gold standard for Authorities and gleif\n*");
        MatchingGoldStandard gsTset_authorities_gleif = new MatchingGoldStandard();
        gsTset_authorities_gleif.loadFromCSVFile(new File("data/GS/GS_authorities_gleif.csv"));


        LinearCombinationMatchingRule<Company,Attribute> matchingRule_authorities_gleif = new LinearCombinationMatchingRule<>(0.7);
        matchingRule_authorities_gleif.activateDebugReport("data/output/debugResultsMatchingRule_authorities_gleif"+".csv",1000,gsTset_authorities_gleif);

        matchingRule_authorities_gleif.addComparator(new CompanyNameComparatorLevenshtein(),0.4);
        matchingRule_authorities_gleif.addComparator(new CompanyYearComparatorAbsoluteDifferenceSimilarity(),0.4);
        matchingRule_authorities_gleif.addComparator(new CompanyNameComparatorJaccard(),0.2);
        //matchingRule_authorities_gleif.addComparator(new CompanyYearComparatorAbsoluteDifferenceSimilarity(),0.1);
        //System.out.println("###################");
        //matchingRule_authorities_gleif.addComparator(new CompanyNumberComparatorJaccard(),0.4);
        //matchingRule_authorities_gleif.addComparator(new CompanyAddressComparatorJaccard(),0.2);

        //SortedNeighbourhoodBlocker<Company, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByNameGenerator(), 30);
        //NoBlocker<Company, Attribute> blocker = new NoBlocker<>();
        StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company,Attribute>(new CompanyBlockingKeyByNameGenerator());
        blocker.setMeasureBlockSizes(true); //attetion
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_authorities_gleif.csv",100);

        MatchingEngine<Company, Attribute> engine_authorities_gleif = new MatchingEngine<>();
        System.out.println("*\n*\tRunning identity resolution for authorities and gleif\n*");

        Processable<Correspondence<Company,Attribute>> correspondences_authorities_gleif = engine_authorities_gleif.runIdentityResolution(data_authorities,data_gleif,null,matchingRule_authorities_gleif,blocker);

        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/authorities_gleif_correspondences.csv"),correspondences_authorities_gleif);
        System.out.println("*\n*\tEvaluating result for authorities and gleif\n*");
        MatchingEvaluator<Company,Attribute> evaluator_authorities_gleif = new MatchingEvaluator<Company,Attribute>();
        Performance perfTest_authorities_gleif = evaluator_authorities_gleif.evaluateMatching(correspondences_authorities_gleif,gsTset_authorities_gleif);
        //print results
        System.out.println("authorities <--> Gleif");
        System.out.println(String.format("Precision:%.4f",perfTest_authorities_gleif.getPrecision()));
        System.out.println(String.format("Recall:%.4f",perfTest_authorities_gleif.getRecall()));
        System.out.println(String.format("F1:%.4f",perfTest_authorities_gleif.getF1()));

    }
}
