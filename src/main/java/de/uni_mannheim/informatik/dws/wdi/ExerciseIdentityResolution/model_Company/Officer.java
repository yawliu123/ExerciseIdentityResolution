package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

public class Officer extends AbstractRecord<Attribute> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String officerName;
    private String position;

    public Officer(String identifier, String provenance){
        super(identifier,provenance);
    }

    public static final Attribute NAME = new Attribute("officerName");

    @Override
    public boolean hasValue(Attribute attribute) {
        if(attribute==NAME){
            return officerName!=null;
        }else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Officer other = (Officer) obj;
        if (officerName == null) {
            if (other.officerName != null)
                return false;
        } else if (!officerName.equals(other.officerName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 + ((officerName == null) ? 0 : officerName.hashCode());
        return result;
    }


    public String getName() {
        return officerName;
    }

    public void setName(String name){
        this.officerName = name;
    }
    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }
    public String toString(){
        return this.officerName+" "+this.position+" ";
    }
}
