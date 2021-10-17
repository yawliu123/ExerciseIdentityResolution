package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Node;

import java.util.List;

public class CompanyXMLReader extends XMLMatchableReader<Company, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<Company, Attribute> dataset) {
        super.initialiseDataset(dataset);

    }
    @Override
    public Company createModelFromElement(Node node, String provenanceInfo) {
        String id = node.getAttributes().getNamedItem("id").getNodeValue();
        Company company = new Company(id,provenanceInfo);
        company.setName(getValueFromChildElement(node,"name"));
        String addr = "";
        try {
            addr = getValueFromChildElement(node,"address");
        } catch (Exception e){
            addr = "EmptyAddress";
        }
        if (addr == null){
            addr = "EmptyAddress";
        }
        company.setAddress(addr);
        company.setCompanyNumber(getValueFromChildElement(node,"companyNumber"));
        int companyYear = 0;
        try {
            String cy = getValueFromChildElement(node,"companyYear");
            if(cy != null) {
                companyYear = Integer.valueOf(cy);
            }else{
                companyYear = 0;
            }

        }catch (Exception e){
            companyYear = 0;
        }
        company.setYear(companyYear);
        company.setState(getValueFromChildElement(node,"state"));
        company.setStatus(getValueFromChildElement(node,"status"));
        int telefon = 0;
        try {
            String tf = getValueFromChildElement(node,"telefon");
            //System.out.println("tf: "+tf);
            if(tf != null) {
                telefon = Integer.valueOf(tf);
            }else {
                telefon = 0;
            }
        }catch (Exception e){
            telefon = 0;
        }
        company.setTelefon(telefon);
        List<Officer> officers = getObjectListFromChildElement(node,"officers","officer",new OfficerXMLReader(),provenanceInfo);
        company.setOfficers(officers);

        company.setOldAuthorities(getValueFromChildElement(node,"oldAuthorities"));

        return company;
    }
}
