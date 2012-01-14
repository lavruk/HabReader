package net.meiolania.apps.habrahabr;

import net.meiolania.apps.habrahabr.api.Login;
import net.meiolania.apps.habrahabr.api.Posts;
import net.meiolania.apps.habrahabr.api.PostsComments;
import android.content.Context;

public class Api{
    private Context context;
    
    public Api(Context context){
        this.context = context;
    }
    
    public Posts getPostsApi(){
        return new Posts();
    }
    
    public Login getLoginApi(){
        return new Login();
    }
    
    public PostsComments getPostsCommentsApi(){
        return new PostsComments();
    }
    
}