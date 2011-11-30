package net.meiolania.apps.habrahabr.data;

public class BlogsData{
    private String title;
    private String statistics;
    private String link;
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setStatistics(String statistics){
        this.statistics = statistics;
    }
    
    public String getStatistics(){
        return statistics;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
    
}