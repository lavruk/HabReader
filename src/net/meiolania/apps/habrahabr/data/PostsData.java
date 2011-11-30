package net.meiolania.apps.habrahabr.data;

public class PostsData{
    private String title;
    private String blog;
    private String link;
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setBlog(String blog){
        this.blog = blog;
    }
    
    public String getBlog(){
        return blog;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
    
}