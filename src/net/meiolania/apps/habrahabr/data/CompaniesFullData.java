package net.meiolania.apps.habrahabr.data;

public class CompaniesFullData extends CompaniesData{
    protected String companyUrl;
    protected String date;
    protected String industries;
    protected String location;
    protected String summary;
    protected String quantity;
    protected String management;
    protected String developmentStages;

    public String getQuantity(){
        return quantity;
    }

    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public String getCompanyUrl(){
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl){
        this.companyUrl = companyUrl;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getIndustries(){
        return industries;
    }

    public void setIndustries(String industries){
        this.industries = industries;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getSummary(){
        return summary;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public String getManagement(){
        return management;
    }

    public void setManagement(String management){
        this.management = management;
    }

    public String getDevelopmentStages(){
        return developmentStages;
    }

    public void setDevelopmentStages(String developmentStages){
        this.developmentStages = developmentStages;
    }
}