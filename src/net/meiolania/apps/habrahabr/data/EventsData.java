package net.meiolania.apps.habrahabr.data;

public class EventsData{
    protected String title;
    protected String detail;
    protected String text;
    protected String date;
    protected String url;

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDetail(){
        return detail;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}