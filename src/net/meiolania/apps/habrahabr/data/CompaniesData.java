package net.meiolania.apps.habrahabr.data;

public class CompaniesData{
    protected String icon;
    protected String url;
    protected String title;
    protected String index;
    protected String description;

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

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

    public String getIndex(){
        return index;
    }

    public void setIndex(String index){
        this.index = index;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    
}