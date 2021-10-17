package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CompanyBlockingKeyByCompanyNumberGenerator extends RecordBlockingKeyGenerator<Company, Attribute> {
    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
     */
    @Override
    public void generateBlockingKeys(Company record, Processable<Correspondence<Attribute, Matchable>> correspondences,
                                     DataIterator<Pair<String, Company>> resultCollector) {

        String[] tokens  = record.getCompanyNumber().split(" ");

        String blockingKeyValue = "";

        for(int i = 0; i <= 2 && i < tokens.length; i++) {
            blockingKeyValue += tokens[i].substring(0, Math.min(2,tokens[i].length())).toUpperCase();
        }

        resultCollector.next(new Pair<>(blockingKeyValue, record));
    }
}
