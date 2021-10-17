package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model_Company;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.LinkedList;
import java.util.List;

public class Company implements Matchable {
    private String id;
    protected String provenance;
    protected String name;
    protected String address;
    protected String companyNumber;
    protected int companyYear;
    protected String state;
    protected String status;
    protected int telefon;
    private List<Officer> officers;
    private String oldAuthorities;

    public Company(String identifier, String provenance){
        this.id = identifier;
        this.provenance = provenance;
        officers = new LinkedList<>();
    }

    //get set methods
    public String getIdentifier(){
        return this.id;
    }

    public void setIdentifier(String id){
        this.id = id;
    }

    public String getProvenance(){
        return this.provenance;
    }

    public void setProvenance(String provenance){
        this.provenance = provenance;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getCompanyNumber(){
        return this.companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }


    public int getYear(){
        return this.companyYear;
    }

    public void setYear(int year){
        this.companyYear = year;
    }

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public int getTelefon(){
        return this.telefon;
    }

    public void setTelefon(int telefon){
        this.telefon = telefon;
    }

    public List<Officer> getOfficers(){
        return this.officers;
    }

    public void setOfficers(List<Officer> officers){
        this.officers = officers;
    }

    public String getOldAuthorities(){
        return this.oldAuthorities;
    }

    public void setOldAuthorities(String oldAuthorities){
        this.oldAuthorities = oldAuthorities;
    }
    @Override
    public String toString(){
        return String.format("[Company %s: %s %s %s %s %s %s %s]",getIdentifier(),getName(),getCompanyNumber(),getAddress(), getOfficers(),getYear(),getStatus(),getOfficers());
    }
    @Override
    public int hashCode(){
        return getIdentifier().hashCode();
    }
    public boolean equals(Object obj){
        if(obj instanceof Company){
            return this.getIdentifier().equals(((Company) obj).getIdentifier());
        }else{
            return false;        }
    }

}
