package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.apache.jena.base.Sys;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CompanyXMLFormatter extends XMLFormatter<Company> {
    OfficerXMLFormatter officerXMLFormatter = new OfficerXMLFormatter();
    @Override
    public Element createRootElement(Document doc){
        return doc.createElement("companies");
    }


    @Override
    public Element createElementFromRecord(Company record, Document doc) {
        Element company = doc.createElement("company");
        company.appendChild(createTextElement("id", record.getIdentifier(), doc));
        company.appendChild(createTextElement("name", record.getName(), doc));
        company.appendChild(createTextElement("address", record.getAddress(), doc));
        company.appendChild(createTextElement("companyNumber",record.getCompanyNumber(),doc));
        company.appendChild(createTextElement("companyYear",Integer.toString(record.getYear()),doc));
        company.appendChild(createTextElement("state",record.getState(),doc));
        company.appendChild(createTextElement("status",record.getStatus(),doc));
        company.appendChild(createTextElement("telefon",Integer.toString(record.getTelefon()),doc));
        company.appendChild(createOfficersElement(record,doc));
        company.appendChild(createTextElement("oldAuthorities",record.getOldAuthorities(),doc));
        return company;
    }

    protected Element createOfficersElement(Company record, Document doc){
        Element officerRoot = officerXMLFormatter.createRootElement(doc);
        for(Officer o: record.getOfficers()){
            System.out.println(o.getName());
            officerRoot.appendChild(officerXMLFormatter.createElementFromRecord(o,doc));
        }
        return officerRoot;
    }

    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }
}
