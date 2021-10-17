package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators_Company;


import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class CompanyTelefonComparatorAbsoluteDifferenceSimilarity implements Comparator<Company, Attribute> {
    private static final long serialVersionUID = 1L;
    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(50);
    private ComparatorLogger comparisonLog;


    public double compare(Company record1, Company record2, Correspondence<Attribute, Matchable> schemaCorrespondences) {

        double n1 = record1.getTelefon();
        double n2 = record2.getTelefon();

        double similarity = sim.calculate(n1, n2);

        if (this.comparisonLog != null) {
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(Double.toString(n1));
            this.comparisonLog.setRecord2Value(Double.toString(n2));

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;
    }


    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }


    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

}
