package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByAddressGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByCompanyNumberGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company.CompanyBlockingKeyByNameGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.CompanyXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Officer;
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
import java.util.List;

public class Machine_Leaning {
    private static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception{
        //loading data
        System.out.println("*\n*\tLoading datasets\n*");
        HashedDataSet<Company, Attribute> data_bisnode = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_bisnode_ocdata.xml"),"/companies/company",data_bisnode);
        //System.out.println(data_bisnode.get());

        HashedDataSet<Company,Attribute> data_ocdata = new HashedDataSet<>();
        new CompanyXMLReader().loadFromXML(new File("data/input/Blocking_ocdata_bisnode.xml"),"companies/company",data_ocdata);

        //Load training

        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTraining_bisnode_ocdata = new MatchingGoldStandard();
        gsTraining_bisnode_ocdata.loadFromCSVFile(new File(
                "data/training/Training_bisnode_ocdata_updated.csv"));



        /**
         * Bisnode and Ocdata
         */
        //String options[] = new String[] {"-B"};
        //String modelType = "LMT"; // use a logistic regression
       // String options[] = new String[]{"-A"};
       // String modelType = "SimpleLogistic";
       // String options[] = new String[] {"-output-debug-info"};
        //String modelType = "RandomTree";
       // String options[] = new String[]{"-S"};
        //String modelType = "J48";

      // String options[] = new String[] {"-output-debug-info"};
       //String modelType = "SMO";
        //String options[] = new String[] {"-U"};
        //String modelType = "RandomTree";
        String options[] = new String[] {"-O"};
        String modelType = "RandomForest";
        //String options[] = new String[] {"-O"};
        //String modelType = "NaiveBayes";
        //String options[] = new String[] {"-do-not-check-capabilities"};
        //String options[] = new String[] {"-additional-stats"};
        //String modelType = "SimpleLinearRegression";
        //String options[] = new String[] {"-E"};
        //String modelType = "KStar";
        //String options[] = new String[] {"-D"};
        //String modelType = "JRip";


        WekaMatchingRule<Company, Attribute> matchingRule_bisnode_ocdata = new WekaMatchingRule<>(0.7, modelType, options);
        matchingRule_bisnode_ocdata.activateDebugReport("data/output/debugResultsMatchingRule_bisnode_ocdata.csv", 1000,
                gsTraining_bisnode_ocdata);

        matchingRule_bisnode_ocdata.addComparator(new CompanyNameComparatorEqual());
        matchingRule_bisnode_ocdata.addComparator(new CompanyAddressComparatorJaccard());
        matchingRule_bisnode_ocdata.addComparator(new CompanyAddressComparatorEqual());
        //matchingRule_bisnode_ocdata.addComparator(new CompanyAddressComparatorJaccardOnNGram());
        matchingRule_bisnode_ocdata.addComparator(new CompanyAddressComparatorLevenshtein());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNameComparatorJaccard());
        //matchingRule_bisnode_ocdata.addComparator(new CompanyNameComparatorJaccardOnNGram());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNameComparatorLevenshtein());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNumberComparatorJaccard());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNumberComparatorEqual());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNumberComparatorJaccardOnNGram());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNumberComparatorLevenshtein());
        matchingRule_bisnode_ocdata.addComparator(new CompanyNumberComparatorJaccard());


        // train the matching rule's model
        System.out.println("*\n*\tLearning matching rule\n*");
        RuleLearner<Company, Attribute> learner_bisnode_ocdata = new RuleLearner<>();
        learner_bisnode_ocdata.learnMatchingRule(data_bisnode, data_ocdata, null, matchingRule_bisnode_ocdata, gsTraining_bisnode_ocdata);
        System.out.println(
                String.format("Matching rule of bisnode_ocdata is:\n%s", matchingRule_bisnode_ocdata.getModelDescription()));

        // create a blocker (blocking strategy)
        // StandardRecordBlocker<Book, Attribute> blocker = new
        // StandardRecordBlocker<Book, Attribute>(new
        // BookBlockingKeyByTitleGenerator());
        //StandardRecordBlocker<Company, Attribute> blocker = new StandardRecordBlocker<Company, Attribute>(new CompanyBlockingKeyByNameGenerator());
        SortedNeighbourhoodBlocker<Company,Attribute,Attribute> blocker = new SortedNeighbourhoodBlocker<>(new CompanyBlockingKeyByCompanyNumberGenerator(),30);
        //blocker.collectBlockSizeData("data_company/output/debugResultsBlocking_bisnode_ocdata.csv", 100);
        //NoBlocker<Book, Attribute> blocker_BX_books = new NoBlocker<>();
        //StandardRecordBlocker<Book, Attribute> blocker_BX_books = new StandardRecordBlocker<Book, Attribute>(new BookBlockingKeyByTitleGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking_bisnode_ocdata.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Company, Attribute> engine_bisnode_ocdata = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Company, Attribute>> correspondences_bisnode_ocdata = engine_bisnode_ocdata.runIdentityResolution(
                data_bisnode, data_ocdata, null, matchingRule_bisnode_ocdata, blocker);

        //write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/bisnode_ocdata.csv"), correspondences_bisnode_ocdata);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest_bisnode_ocdata = new MatchingGoldStandard();
        //here need to edit later, because there is no train dataset
        gsTest_bisnode_ocdata.loadFromCSVFile(new File("data/GS/GS_bisnode_ocdata_updated_last_phase.csv"));

        // evaluate your result
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Company, Attribute> evaluator_bisnode_ocdata = new MatchingEvaluator<Company, Attribute>();
        Performance perfTest_bisnode_ocdata = evaluator_bisnode_ocdata.evaluateMatching(correspondences_bisnode_ocdata, gsTest_bisnode_ocdata);

        // print the evaluation result
        System.out.println("bisnode <-> ocdata");
        System.out.println(String.format("Precision: %.4f", perfTest_bisnode_ocdata.getPrecision()));
        System.out.println(String.format("Recall: %.4f", perfTest_bisnode_ocdata.getRecall()));
        System.out.println(String.format("F1: %.4f", perfTest_bisnode_ocdata.getF1()));

    }
}
