package net.meiolania.apps.habrahabr.data;

public class EventsFullData extends EventsData{
    protected String location;
    protected String pay;
    protected String site;
    protected String logo;

    public String getLogo(){
        return logo;
    }

    public void setLogo(String logo){
        this.logo = logo;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getPay(){
        return pay;
    }

    public void setPay(String pay){
        this.pay = pay;
    }

    public String getSite(){
        return site;
    }

    public void setSite(String site){
        this.site = site;
    }

}