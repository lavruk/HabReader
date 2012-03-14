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

import net.meiolania.apps.habrahabr.api.HubsApi;
import net.meiolania.apps.habrahabr.api.CompaniesApi;
import net.meiolania.apps.habrahabr.api.EventsApi;
import net.meiolania.apps.habrahabr.api.LoginApi;
import net.meiolania.apps.habrahabr.api.PeopleApi;
import net.meiolania.apps.habrahabr.api.PostsApi;
import net.meiolania.apps.habrahabr.api.PostsCommentsApi;
import net.meiolania.apps.habrahabr.api.QaApi;
import net.meiolania.apps.habrahabr.api.QaCommentsApi;

public final class Api{
    private static Api apiInstance = null;
    
    public static Api getInstance(){
        if(apiInstance == null)
            return (apiInstance = new Api());
        else
            return apiInstance;
    }
    
    public HubsApi getBlogsApi(){
        return HubsApi.getInstance();
    }
    
    public PostsApi getPostsApi(){
        return PostsApi.getInstance();
    }
    
    public LoginApi getLoginApi(){
        return new LoginApi();
    }
    
    public PostsCommentsApi getPostsCommentsApi(){
        return PostsCommentsApi.getInstance();
    }
    
    public QaApi getQaApi(){
        return QaApi.getInstance();
    }
    
    public QaCommentsApi getQaCommentsApi(){
        return QaCommentsApi.getInstance();
    }
    
    public EventsApi getEventsApi(){
        return EventsApi.getInstance();
    }
    
    public CompaniesApi getCompaniesApi(){
        return CompaniesApi.getInstance();
    }
    
    public PeopleApi getPeopleApi(){
        return PeopleApi.getInstance();
    }
    
}