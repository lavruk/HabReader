package net.meiolania.apps.habrahabr.data;

public class PostsFullData extends PostsData{
    protected String content;
    
    public void setContent(String content){
        this.content = content;
    }
    
    public String getContent(){
        return content;
    }
    
}