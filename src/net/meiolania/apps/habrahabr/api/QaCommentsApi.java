package net.meiolania.apps.habrahabr.api;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QaCommentsApi{
    
    public ArrayList<CommentsData> getComments(String link) throws IOException{
        ArrayList<CommentsData> commentsDataList = new ArrayList<CommentsData>();
        
        Document document = Jsoup.connect(link).get();
        Elements comments = document.select("div.answer");

        for(Element comment : comments){
            CommentsData commentsData = new CommentsData();
            
            Element userName = comment.select("a.username").first();
            Element message = comment.select("div.message").first();
            Element score = comment.select("span.score").first();
            
            commentsData.setAuthor(userName.text());
            commentsData.setAuthorLink(userName.attr("abs:href"));
            commentsData.setScore(UIUtils.parseCommentsScore(score.text()));
            commentsData.setText(message.text());
            
            commentsDataList.add(commentsData);
            
            
            Elements commentsItem = comment.select("div.comment_item");
            for(Element commentItem : commentsItem){
                commentsData = new CommentsData();
                
                userName = commentItem.select("span.username > a").first();
                message = commentItem.select("span.text").first();
                
                commentsData.setAuthor(userName.text());
                commentsData.setAuthorLink(userName.attr("abs:href"));
                commentsData.setScore(0);
                commentsData.setText(message.text());
                
                commentsDataList.add(commentsData);
            }
        }
        return commentsDataList;
    }
    
}