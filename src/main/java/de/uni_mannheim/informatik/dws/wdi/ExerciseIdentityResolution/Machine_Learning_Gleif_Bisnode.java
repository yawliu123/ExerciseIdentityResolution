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

public class Machine_Learning_Gleif_Bisnode {
    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception{
        //loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Company, Attribute> data_bisnode = new HashedDataSet<>();
        //new CompanyXMLReader().loadFromXML(new File("data_company/input/Gleif_Bisnode_Matching.xml"),"/companies/company",data_bisnode);
        //System.out.println(data_bisnode.get());
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_bisnode_gleif.xml"),"/companies/company",data_bisnode);

        HashedDataSet<Company,Attribute> data_gleif = new HashedDataSet<>();
        //new CompanyXMLReader().loadFromXML(new File("data_company/input/Bisnode_Gleif_Matching.xml"),"companies/company",data_gleif);
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_gleif_bisnode.xml"),"companies/company",data_gleif);
        //Load GS

        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTraining_gleif_bisnode = new MatchingGoldStandard();
       // gsTraining_gleif_bisnode.loadFromCSVFile(new File(
         //       "data/training/Gleif_Bisnode_training.csv"));
        gsTraining_gleif_bisnode.loadFromCSVFile(new File(
                "data/training/Training_gleif_bisnode_updated.csv"));


        /**
         * Bisnode and Ocdata
         */
        //String options[] = new String[] {"-B"};
        //String modelType = "LMT"; // use a logistic regression
        //String options[] = new String[]{"-A"};
        //String modelType = "SimpleLogistic";
        //
       // String options[] = new String[] {"-J"};
        //String modelType = "J48";
        //String options[] = new String[] {"-U"};
        //String modelType = "RandomTree";
       // String options[] = new String[] {"-B"};
        //String modelType = "RandomTree";
        //String options[] = new String[] {"-O"};
        //String modelType = "RandomForest";
        String options[] = new String[] {"-A"};
        String modelType = "SimpleLogistic";
        //tring options[] = new String[] {"-additional-stats"};
        //String modelType = " SimpleLinearRegression";
        //String options[] = new String[] {"-E"};
        //String modelType = "KStar";
        //String options[] = new String[] {"-M"};
        //String modelType = "SMO";
       // String options[] = new String[] {"-print"};
        //String modelType = "RandomForest";

        //String options[] = new String[] {"-K"};
       // String modelType = "NaiveBayes";
        WekaMatchingRule<Company, Attribute> matchingRule_gleif_bisnode = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_gleif_bisnode.activateDebugReport("data/output/debugResultsMatchingRule_gleif_bisnode.csv", 1000,
                gsTraining_gleif_bisnode);

        matchingRule_gleif_bisnode.addComparator(new CompanyNameComparatorEqual());
        matchingRule_gleif_bisnode.addComparator(new CompanyAddressComparatorJaccard());
        matchingRule_gleif_bisnode.addComparator(new CompanyAddressComparatorEqual());
        //matchingRule_gleif_bisnode.addComparator(new CompanyAddressComparatorJaccardOnNGram());
        matchingRule_gleif_bisnode.addComparator(new CompanyAddressComparatorLevenshtein());
        matchingRule_gleif_bisnode.addComparator(new CompanyNameComparatorJaccard());
        //matchingRule_gleif_bisnode.addComparator(new CompanyNameComparatorJaccardOnNGram());
        matchingRule_gleif_bisnode.addComparator(new CompanyNameComparatorLevenshtein());
        matchingRule_gleif_bisnode.addComparator(new CompanyNumberComparatorJaccard());
        matchingRule_gleif_bisnode.addComparator(new CompanyNumberComparatorEqual());
        //matchingRule_gleif_bisnode.addComparator(new CompanyNumberComparatorJaccardOnNGram());
        matchingRule_gleif_bisnode.addComparator(new CompanyNumberComparatorLevenshtein());
        matchingRule_gleif_bisnode.addComparator(new CompanyNumberComparatorJaccard());


        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Company, Attribute> learner_gleif_bisnode = new RuleLearner<>();
        learner_gleif_bisnode.learnMatchingRule(data_bisnode, data_gleif, null, matchingRule_gleif_bisnode, gsTraining_gleif_bisnode);
        System.out.println(
                String.format("Matching rule of gleif_bisnode is:\n%s", matchingRule_gleif_bisnode.getModelDescription()));

        // create a blocker (blocking strategy)
        // StandardRecordBlocker<Book, Attribute> blocker = new
        // StandardRecordBlocker<Book, Attribute>(new
        // BookBlockingKeyByTitleGenerator());
       // StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company, Attribute>(new CompanyBlockingKeyByNameGenerator());
        SortedNeighbourhoodBlocker<Company,Attribute,Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByNameGenerator(),30);
        //blocker.collectBlockSizeData("data_company/output/debugResultsBlocking_gleif_bisnode.csv", 100);
        //NoBlocker<Book, Attribute> blocker_BX_books = new NoBlocker<>();
        //StandardRecordBlocker<Book, Attribute> blocker_BX_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_gleif_bisnode.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Company, Attribute> engine_gleif_bisnode = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Company, Attribute>> correspondences_gleif_bisnode = engine_gleif_bisnode.runIdentityResolution(
                data_gleif, data_bisnode, null, matchingRule_gleif_bisnode, blocker);

        //write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/gleif_bisnode.csv"), correspondences_gleif_bisnode);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest_gleif_bisnode = new MatchingGoldStandard();
        //here need to edit later, because there is no train dataset
        gsTest_gleif_bisnode.loadFromCSVFile(new File("data/GS/GS_gleif_bisnode_updated_last_phase.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Company, Attribute> evaluator_gleif_bisnode = new MatchingEvaluator<Company, Attribute>();
        Performance perfTest_gleif_bisnode = evaluator_gleif_bisnode.evaluateMatching(correspondences_gleif_bisnode, gsTest_gleif_bisnode);

        // print the evaluation result
        System.out.println("bisnode <-> gleif");
        System.out.println(String.format("Precision: %.4f", perfTest_gleif_bisnode.getPrecision()));
        System.out.println(String.format("Recall: %.4f", perfTest_gleif_bisnode.getRecall()));
        System.out.println(String.format("F1: %.4f", perfTest_gleif_bisnode.getF1()));

    }
}
