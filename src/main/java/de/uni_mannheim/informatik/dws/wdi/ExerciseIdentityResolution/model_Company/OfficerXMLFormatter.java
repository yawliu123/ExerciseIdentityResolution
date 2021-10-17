package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OfficerXMLFormatter extends XMLFormatter<Officer> {
    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("officers");
    }

    @Override
    public Element createElementFromRecord(Officer record, Document doc) {
        Element officer = doc.createElement("officer");
        System.out.println(record.getName());
        officer.appendChild(createTextElement("officerName",record.getName(),doc));
        officer.appendChild(createTextElement("position",record.getPosition(),doc));
        return officer;
    }
}
