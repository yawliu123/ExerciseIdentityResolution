package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.CompanyNameComparatorEqual;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.CompanyNameComparatorJaccardOnNGram;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.CompanyNameComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.CompanyYearComparatorAbsoluteDifferenceSimilarity;
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

public class Linear_Authorities_Ocdata {private static final Logger logger = WinterLogManager.activateLogger("default");


    public static void main(String[] args) throws Exception{
        //loading data
        System.out.println("*\n*\tLoading datasets\n*");

        HashedDataSet<Company, Attribute> data_authorities = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_authorities_ocdata.xml"),"/companies/company",data_authorities);


        HashedDataSet<Company,Attribute> data_ocdata = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_ocdata_authorities.xml"),"/companies/company",data_ocdata);

        /**
         * Gleif and Authorities
         */

        System.out.println("*\n*\tLoading gold standard for Authorities and ocdata\n*");
        MatchingGoldStandard gsTset_authorities_ocdata = new MatchingGoldStandard();
        gsTset_authorities_ocdata.loadFromCSVFile(new File("data/GS/GS_authorities_ocdata.csv"));


        LinearCombinationMatchingRule<Company,Attribute> matchingRule_authorities_ocdata = new LinearCombinationMatchingRule<>(0.6);
        matchingRule_authorities_ocdata.activateDebugReport("data/output/debugResultsMatchingRule_authorities_ocdata"+".csv",1000,gsTset_authorities_ocdata);

        matchingRule_authorities_ocdata.addComparator(new CompanyNameComparatorLevenshtein(),0.4);
        matchingRule_authorities_ocdata.addComparator(new CompanyYearComparatorAbsoluteDifferenceSimilarity(),0.4);
        matchingRule_authorities_ocdata.addComparator(new CompanyNameComparatorJaccardOnNGram(),0.2);
        //System.out.println("###################");
        //matchingRule_authorities_ocdata.addComparator(new CompanyNumberComparatorJaccard(),0.4);
        //matchingRule_authorities_ocdata.addComparator(new CompanyAddressComparatorJaccard(),0.2);

        SortedNeighbourhoodBlocker<Company, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByNameGenerator(), 30);
        //NoBlocker<Company, Attribute> blocker = new NoBlocker<>();
        //StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company,Attribute>(new CompanyBlockingKeyByNameGenerator());
        blocker.setMeasureBlockSizes(true); //attetion
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_authorities_ocdata.csv",100);

        MatchingEngine<Company, Attribute> engine_authorities_ocdata = new MatchingEngine<>();
        System.out.println("*\n*\tRunning identity resolution for authorities and ocdata\n*");

        Processable<Correspondence<Company,Attribute>> correspondences_authorities_ocdata = engine_authorities_ocdata.runIdentityResolution(data_authorities,data_ocdata,null,matchingRule_authorities_ocdata,blocker);

        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/authorities_ocdata_correspondences.csv"),correspondences_authorities_ocdata);
        System.out.println("*\n*\tEvaluating result for authorities and ocdata\n*");
        MatchingEvaluator<Company,Attribute> evaluator_authorities_ocdata = new MatchingEvaluator<Company,Attribute>();
        Performance perfTest_authorities_ocdata = evaluator_authorities_ocdata.evaluateMatching(correspondences_authorities_ocdata,gsTset_authorities_ocdata);
        //print results
        System.out.println("authorities <--> Ocdata");
        System.out.println(String.format("Precision:%.4f",perfTest_authorities_ocdata.getPrecision()));
        System.out.println(String.format("Recall:%.4f",perfTest_authorities_ocdata.getRecall()));
        System.out.println(String.format("F1:%.4f",perfTest_authorities_ocdata.getF1()));

    }
}
