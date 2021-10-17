package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Officer;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.list.MaximumOfContainment;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyOfficerComparatorMaximumOfContainment implements Comparator<Company, Attribute> {
    private static final long serialVersionUID = 1L;
    private MaximumOfContainment<String> sim = new MaximumOfContainment<String>();

    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Company record1, Company record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

        List<Officer> list1 = record1.getOfficers();
        List<Officer> list2 = record2.getOfficers();

        List<String> l1 = new LinkedList<String>();
        List<String> l2 = new LinkedList<String>();

        String print1 = list1.stream().map(Officer::getName).collect(Collectors.joining(", "));
        String print2 = list2.stream().map(Officer::getName).collect(Collectors.joining(", "));

        for(int i=0; i<list1.size(); i++){
            if(preprocesString(list1.get(i).getName()) != null)
                l1.add(preprocesString(list1.get(i).getName()));
            else l1.add("");
        }

        for(int i=0; i<list2.size(); i++){
            if(preprocesString(list2.get(i).getName()) != null)
                l2.add(preprocesString(list2.get(i).getName()));
            else l2.add("");
        }

        double similarity = sim.calculate(l1,l2);

        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getClass().getName());


            this.comparisonLog.setRecord1Value(print1);


            this.comparisonLog.setRecord2Value(print2);

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }

        return similarity;
    }

    public String preprocesString(String s) {
        // Normalize Spelling: lowercase, remove punctuation and remove non-ASCII characters
        s = s.toLowerCase();
        s = s.replaceAll("&amp;amp", "");
        s = s.replaceAll("\\p{Punct}", "");
        s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

}
