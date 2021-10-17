package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByAddressGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByCompanyNumberGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.CompanyXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
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

public class Linear_Gleif_Bisnode {
    private static final Logger logger = WinterLogManager.activateLogger("default");


    public static void main(String[] args) throws Exception {
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Company, Attribute> data_gleif = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_gleif_bisnode.xml"),"/companies/company",data_gleif);

        HashedDataSet<Company,Attribute> data_Bisnode = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_bisnode_gleif.xml"),"/companies/company",data_Bisnode);

        //load Gold standard
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        //gsTest.loadFromCSVFile(new File(
        //        "data_company/goldstandard/GS_gleif_Bisnode.csv"));
        gsTest.loadFromCSVFile(new File(
                "data/GS/GS_gleif_bisnode_updated_last_phase.csv"));
        //create a matching rule
        LinearCombinationMatchingRule<Company, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.9);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTest);

        //add comparators
        matchingRule.addComparator(new CompanyNameComparatorLevenshtein(),0.4);
        matchingRule.addComparator(new CompanyTelefonComparatorAbsoluteDifferenceSimilarity(),0.2);
        matchingRule.addComparator(new CompanyAddressComparatorLevenshtein(),0.4);
        //Blocker
        //NoBlocker<Company, Attribute> blocker = new NoBlocker<>();
        StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company, Attribute>(new CompanyBlockingKeyByNameGenerator());
        //SortedNeighbourhoodBlocker<Company, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByNameGenerator(), 30);

        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
        //Initialize Matching Engine
        MatchingEngine<Company, Attribute> engine = new MatchingEngine<>();
        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Company, Attribute>> correspondences = engine.runIdentityResolution(
                data_gleif, data_Bisnode, null, matchingRule,
                blocker);
        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/company_gleif_Bisnode_correspondences.csv"), correspondences);
        System.out.println("*\n*\tEvaluating result\n*");
        System.out.println("###########################################");
        System.out.println(correspondences.get().size());
        System.out.println("###########################################");
        // evaluate your result
        MatchingEvaluator<Company, Attribute> evaluator = new MatchingEvaluator<Company, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);
        // print the evaluation result
        System.out.println("Company <-> companies");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}
