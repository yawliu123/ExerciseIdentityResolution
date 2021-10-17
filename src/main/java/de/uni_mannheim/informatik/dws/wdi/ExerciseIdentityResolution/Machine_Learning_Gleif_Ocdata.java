package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByAddressGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByCompanyNumberGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.CompanyXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
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

public class Machine_Learning_Gleif_Ocdata {
    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception{
        //loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Company, Attribute> data_ocdata = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_ocdata_gleif.xml"),"/companies/company",data_ocdata);
        //System.out.println(data_ocdata.get());


        HashedDataSet<Company,Attribute> data_gleif = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_gleif_ocdata.xml"),"companies/company",data_gleif);

        //Load GS

        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTraining_gleif_ocdata = new MatchingGoldStandard();
        gsTraining_gleif_ocdata.loadFromCSVFile(new File(
                "data/training/Gleif_Ocdata_training.csv"));



        /**
         * Bisnode and Ocdata
         */
        //String options[] = new String[] {"-B"};
        //String modelType = "LMT"; // use a logistic regression
        //String options[] = new String[]{"-S"};
        //String modelType = "SimpleLogistic";
        //String options[] = new String[] {"-K"};
        //String modelType = "NaiveBayes";
        //String options[] = new String[] {"-P"};
        // String modelType = "SimpleLinearRegression";
        //String options[] = new String[]{"-J"};
        //String modelType = "J48";
        //String options[] = new String[]{"-output-debug-info"};
        //String modelType = "SMO";
       // String options[] = new String[]{"-B"};
       // String modelType = "RandomTree";

        //String options[] = new String[]{"-output-debug-info"};
        //String modelType = "RandomForest";

        String options[] = new String[]{"-K"};
        String modelType = "NaiveBayes";
       // String options[] = new String[]{"-E"};
       // String modelType = "KStar";
        WekaMatchingRule<Company, Attribute> matchingRule_gleif_ocdata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_gleif_ocdata.activateDebugReport("data/output/debugResultsMatchingRule_gleif_ocdata.csv", 1000,
                gsTraining_gleif_ocdata);

        matchingRule_gleif_ocdata.addComparator(new CompanyNameComparatorEqual());
        matchingRule_gleif_ocdata.addComparator(new CompanyAddressComparatorJaccard());
        matchingRule_gleif_ocdata.addComparator(new CompanyAddressComparatorEqual());
        //matchingRule_gleif_ocdata.addComparator(new CompanyAddressComparatorJaccardOnNGram());
        matchingRule_gleif_ocdata.addComparator(new CompanyAddressComparatorLevenshtein());
        matchingRule_gleif_ocdata.addComparator(new CompanyNameComparatorJaccard());
        //matchingRule_gleif_ocdata.addComparator(new CompanyNameComparatorJaccardOnNGram());
        matchingRule_gleif_ocdata.addComparator(new CompanyNameComparatorLevenshtein());
        matchingRule_gleif_ocdata.addComparator(new CompanyNumberComparatorJaccard());
        matchingRule_gleif_ocdata.addComparator(new CompanyNumberComparatorEqual());
        //matchingRule_gleif_ocdata.addComparator(new CompanyNumberComparatorJaccardOnNGram());
        matchingRule_gleif_ocdata.addComparator(new CompanyNumberComparatorLevenshtein());
        matchingRule_gleif_ocdata.addComparator(new CompanyNumberComparatorJaccard());
        //matchingRule_gleif_ocdata.addComparator(new CompanyOfficerComparatorMaximumOfContainment());
        //matchingRule_gleif_ocdata.addComparator(new CompanyOfficerComparatorMaxSimilarity());
        //matchingRule_gleif_ocdata.addComparator(new CompanyOfficerComparatorOverlapSimilarity());


        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Company, Attribute> learner_gleif_ocdata = new RuleLearner<>();
        learner_gleif_ocdata.learnMatchingRule(data_ocdata, data_gleif, null, matchingRule_gleif_ocdata, gsTraining_gleif_ocdata);
        System.out.println(
                String.format("Matching rule of gleif_ocdata is:\n%s", matchingRule_gleif_ocdata.getModelDescription()));

        // create a blocker (blocking strategy)
        // StandardRecordBlocker<Book, Attribute> blocker = new
        // StandardRecordBlocker<Book, Attribute>(new
        // BookBlockingKeyByTitleGenerator());
        //StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company, Attribute>(new CompanyBlockingKeyByNameGenerator());
        SortedNeighbourhoodBlocker<Company,Attribute,Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByNameGenerator(),60);
        //blocker.collectBlockSizeData("data_company/output/debugResultsBlocking_gleif_ocdata.csv", 100);
        //NoBlocker<Book, Attribute> blocker_BX_books = new NoBlocker<>();
        //StandardRecordBlocker<Book, Attribute> blocker_BX_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_gleif_ocdata.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Company, Attribute> engine_gleif_ocdata = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Company, Attribute>> correspondences_gleif_ocdata = engine_gleif_ocdata.runIdentityResolution(
                data_gleif, data_ocdata, null, matchingRule_gleif_ocdata, blocker);

        //write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/gleif_ocdata.csv"), correspondences_gleif_ocdata);
        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest_gleif_ocdata = new MatchingGoldStandard();
        //here need to edit later, because there is no train dataset
        gsTest_gleif_ocdata.loadFromCSVFile(new File("data/GS/GS_gleif_ocdata_updated_last_phase.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Company, Attribute> evaluator_gleif_ocdata = new MatchingEvaluator<Company, Attribute>();
        Performance perfTest_gleif_ocdata = evaluator_gleif_ocdata.evaluateMatching(correspondences_gleif_ocdata, gsTest_gleif_ocdata);

        // print the evaluation result
        System.out.println("ocdata <-> gleif");
        System.out.println(String.format("Precision: %.4f", perfTest_gleif_ocdata.getPrecision()));
        System.out.println(String.format("Recall: %.4f", perfTest_gleif_ocdata.getRecall()));
        System.out.println(String.format("F1: %.4f", perfTest_gleif_ocdata.getF1()));

    }
}
