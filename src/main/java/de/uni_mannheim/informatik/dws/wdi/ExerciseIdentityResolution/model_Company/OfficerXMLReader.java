package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

public class OfficerXMLReader extends XMLMatchableReader<Officer, Attribute> {

    @Override
    public Officer createModelFromElement(Node node, String s) {
        String id = getValueFromChildElement(node,"id");

        //create object
        Officer officer = new Officer(id, s);
        officer.setName(getValueFromChildElement(node,"officerName"));
        officer.setPosition(getValueFromChildElement(node,"position"));
        return officer;
    }
}
