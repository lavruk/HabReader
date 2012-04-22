package net.meiolania.apps.habrahabr.data;

public class QaFullData extends QaData{
    protected String context;
    protected String tags;

    public String getContext(){
        return context;
    }

    public void setContext(String context){
        this.context = context;
    }

    public String getTags(){
        return tags;
    }

    public void setTags(String tags){
        this.tags = tags;
    }

}