package net.meiolania.apps.habrahabr.data;

public class PeopleFullData extends PeopleData{
    protected String fullname;
    protected String birthday;
    protected String summary;
    protected String interests;

    public String getFullname(){
        return fullname;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getSummary(){
        return summary;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public String getInterests(){
        return interests;
    }

    public void setInterests(String interests){
        this.interests = interests;
    }

}