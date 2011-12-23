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

package net.meiolania.apps.habrahabr.widgets;

import java.io.IOException;

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.Posts;
import net.meiolania.apps.habrahabr.activities.PostsShow;
import net.meiolania.apps.habrahabr.api.Connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class SmallPostsWidget extends AppWidgetProvider{

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        Preferences preferences = new Preferences(context);
        
        if(!preferences.isUse3g() && Connection.isMobileNetwork(context))
            return;
        if(!preferences.isRoaming() && Connection.isRoaming(context))
            return;
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service{

        @Override
        public void onStart(Intent intent, int startId){
            RemoteViews updateViews = buildUpdate(this);

            ComponentName thisWidget = new ComponentName(this, SmallPostsWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        public RemoteViews buildUpdate(Context context){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_small_posts);
            try{
                Document document = Jsoup.connect("http://habrahabr.ru/blogs").get();
                Element post = document.select("div.post").first();

                Element title = post.select("a.post_title").first();
                Element blog = post.select("a.blog_title").first();
                
                if(title.text().length() > 0){
                    views.setTextViewText(R.id.title, title.text());
                    views.setTextViewText(R.id.blog, blog.text());
                
                    /* intent for post */
                    Intent intentPost = new Intent(context.getApplicationContext(), PostsShow.class);
                    intentPost.putExtra("link", title.attr("abs:href"));

                    PendingIntent pendingIntentPost = PendingIntent.getActivity(context.getApplicationContext(), 
                                                                                0, 
                                                                                intentPost,
                                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.title, pendingIntentPost);
                
                    /* intent for blog */
                    Intent intentBlog = new Intent(context.getApplicationContext(), Posts.class);
                    intentBlog.putExtra("link", blog.attr("abs:href"));
                
                    PendingIntent pendingIntentBlog = PendingIntent.getActivity(context.getApplicationContext(), 
                                                                                0,
                                                                                intentBlog, 
                                                                                PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.blog, pendingIntentBlog);
                }else{
                    views.setTextViewText(R.id.title, getString(R.string.widget_error));
                    views.setTextViewText(R.id.blog, "");
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return views;
        }

        @Override
        public IBinder onBind(Intent intent){
            return null;
        }

    }

}