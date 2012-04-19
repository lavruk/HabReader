package net.meiolania.apps.habrahabr.data;

public class HubsData{
    protected String title;
    protected String url;
    protected String category;
    protected String index;
    protected String stat;

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

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getIndex(){
        return index;
    }

    public void setIndex(String index){
        this.index = index;
    }

    public String getStat(){
        return stat;
    }

    public void setStat(String stat){
        this.stat = stat;
    }

}