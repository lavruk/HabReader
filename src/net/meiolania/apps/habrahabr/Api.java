package net.meiolania.apps.habrahabr;

import net.meiolania.apps.habrahabr.api.LoginApi;
import net.meiolania.apps.habrahabr.api.PostsApi;
import net.meiolania.apps.habrahabr.api.PostsCommentsApi;
import android.content.Context;

public class Api{
    private Context context;
    
    public Api(Context context){
        this.context = context;
    }
    
    public PostsApi getPostsApi(){
        return new PostsApi();
    }
    
    public LoginApi getLoginApi(){
        return new LoginApi();
    }
    
    public PostsCommentsApi getPostsCommentsApi(){
        return new PostsCommentsApi();
    }
    
}