package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.CommentsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PostsCommentsApi{
    
    public ArrayList<CommentsData> getComments(String link) throws IOException{
        ArrayList<CommentsData> commentsDataList = new ArrayList<CommentsData>();
        
        Document document = Jsoup.connect(link).get();
        Elements comments = document.select("div.comment_item");

        for(Element comment : comments){
            CommentsData commentsData = new CommentsData();

            Element userName = comment.select("a.username").first();
            Element message = comment.select("div.message").first();
            Element score = comment.select("span.score").first();

            commentsData.setAuthor(userName.text());
            commentsData.setAuthorLink(userName.attr("abs:href"));
            commentsData.setText(message.text());
            commentsData.setScore(PostsCommentsApi.parseScore(score.text()));

            commentsDataList.add(commentsData);
        }
        
        return commentsDataList;
    }
    
    public static int parseScore(String score){
        int commentRating = score.charAt(0) == '–' ? -1 : +1;
        try{
            commentRating *= Integer.valueOf(score.substring(1));
        }
        catch(NumberFormatException e){
            commentRating = 0;
        }
        return commentRating;
    }

}