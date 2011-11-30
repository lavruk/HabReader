package net.meiolania.apps.habrahabr.data;

public class CommentsData{
    private String author;
    private String authorLink;
    private String text;
    private short level;

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthorLink(){
        return authorLink;
    }

    public void setAuthorLink(String authorLink){
        this.authorLink = authorLink;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public short getLevel(){
        return level;
    }

    public void setLevel(short level){
        this.level = level;
    }

}