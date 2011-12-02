package net.meiolania.apps.habrahabr.data;

public class CommentsData{
    private String author;
    private String authorLink;
    private String text;

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

}