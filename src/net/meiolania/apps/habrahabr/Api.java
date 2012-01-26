/*
   Copyright (C) 2011 Andrey Zaytsev <a.einsam@gmail.com>
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
  
        http://www.apache.org/licenses/LICENSE-2.0
  
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package net.meiolania.apps.habrahabr;

import net.meiolania.apps.habrahabr.api.BlogsApi;
import net.meiolania.apps.habrahabr.api.CompaniesApi;
import net.meiolania.apps.habrahabr.api.EventsApi;
import net.meiolania.apps.habrahabr.api.LoginApi;
import net.meiolania.apps.habrahabr.api.PeopleApi;
import net.meiolania.apps.habrahabr.api.PostsApi;
import net.meiolania.apps.habrahabr.api.PostsCommentsApi;
import net.meiolania.apps.habrahabr.api.QaApi;
import net.meiolania.apps.habrahabr.api.QaCommentsApi;
import android.content.Context;

public class Api{
    private Context context;
    
    public Api(Context context){
        this.context = context;
    }
    
    public BlogsApi getBlogsApi(){
        return new BlogsApi();
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
    
    public QaApi getQaApi(){
        return new QaApi();
    }
    
    public QaCommentsApi getQaCommentsApi(){
        return new QaCommentsApi();
    }
    
    public EventsApi getEventsApi(){
        return new EventsApi();
    }
    
    public CompaniesApi getCompaniesApi(){
        return new CompaniesApi();
    }
    
    public PeopleApi getPeopleApi(){
        return new PeopleApi();
    }
    
}