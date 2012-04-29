package net.meiolania.apps.habrahabr.data;

public class QaFullData extends QaData{
    protected String content;
    protected String tags;

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getTags(){
        return tags;
    }

    public void setTags(String tags){
        this.tags = tags;
    }

}