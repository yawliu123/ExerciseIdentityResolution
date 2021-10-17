package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.JaccardOnNGramsSimilarity;

public class CompanyNameComparatorJaccardOnNGram implements Comparator<Company, Attribute> {
    private static final long serialVersionUID = 1L;
    private JaccardOnNGramsSimilarity sim = new JaccardOnNGramsSimilarity(1);

    private ComparatorLogger comparisonLog;
    @Override
    public double compare(Company record1, Company record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {
        String s1 = record1.getName();
        String s2 = record2.getName();

        if (s1 != null) {
            s1 = preprocesString(s1);
        } else {
            s1 = "null";
        }

        if (s2 != null) {
            s2 = preprocesString(s2);
        } else {
            s2 = "null";
        }

        // calculate similarity
        double similarity = sim.calculate(s1, s2);

        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getClass().getName());
            this.comparisonLog.setRecord1Value(s1);
            this.comparisonLog.setRecord2Value(s2);
            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }

        return similarity;
    }
    public String preprocesString(String s) {
        // Normalize Spelling: lowercase and remove punctuation
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
