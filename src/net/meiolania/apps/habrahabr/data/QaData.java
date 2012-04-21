package net.meiolania.apps.habrahabr.data;

public class QaData{
    protected String title;
    protected String hubs;
    protected String url;
    protected String date;
    protected String author;
    protected String answers;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getHubs(){
        return hubs;
    }

    public void setHubs(String hubs){
        this.hubs = hubs;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAnswers(){
        return answers;
    }

    public void setAnswers(String answers){
        this.answers = answers;
    }

}