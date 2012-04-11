package net.meiolania.apps.habrahabr.data;

public class PostsData{
    protected String title;
    protected String url;
    protected String hubs;
    protected String author;
    protected String date;
    protected int comments;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getHubs(){
        return hubs;
    }

    public void setHubs(String hubs){
        this.hubs = hubs;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    public String getDate(){
        return date;
    }

    public int getComments(){
        return comments;
    }

    public void setComments(int comments){
        this.comments = comments;
    }

}