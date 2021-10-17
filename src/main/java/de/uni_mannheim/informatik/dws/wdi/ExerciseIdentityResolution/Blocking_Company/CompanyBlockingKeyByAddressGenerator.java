package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking_Company;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company.Company;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CompanyBlockingKeyByAddressGenerator extends RecordBlockingKeyGenerator<Company, Attribute> {
    @Override
    public void generateBlockingKeys(Company record, Processable<Correspondence<Attribute, Matchable>> correspondences, DataIterator<Pair<String, Company>> resultCollector) {
        String[] tokens  = record.getAddress().split(" ");

        String blockingKeyValue = "";

        for(int i = 0; i <= 2 && i < tokens.length; i++) {
            blockingKeyValue += tokens[i].substring(0, Math.min(2,tokens[i].length())).toUpperCase();
        }

        resultCollector.next(new Pair<>(blockingKeyValue, record));
    }
}
