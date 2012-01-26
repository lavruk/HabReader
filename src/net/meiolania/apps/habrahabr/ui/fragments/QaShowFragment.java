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

package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.activities.QaCommentsActivity;
import net.meiolania.apps.habrahabr.ui.activities.QaShowActivity;
import net.meiolania.apps.habrahabr.utils.VibrateUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class QaShowFragment extends ApplicationFragment{
    private String link;
    private boolean isFullView = false;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;
        
        setHasOptionsMenu(true);
        
        View view = inflater.inflate(R.layout.qa_show_fragment, container, false);
        
        if(link != null && !link.isEmpty())
            loadQuestion();
        
        if(isFullView){
            Button more = (Button)view.findViewById(R.id.more);
            more.setVisibility(View.GONE);
        }else{
            Button more = (Button)view.findViewById(R.id.more);
            more.setOnClickListener(new OnClickListener(){
                public void onClick(View v){
                    startShowActivity();
                }
            });
        }
        
        return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.qa_show_fragment, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(preferences.isVibrate())
            VibrateUtils.doVibrate(getActivity());
        switch(item.getItemId()){
            case R.id.show_comments:
                startCommentsActivity();
                break;
        }
        return true;
    }
    
    private void startCommentsActivity(){
        Intent intent = new Intent(getActivity(), QaCommentsActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
    
    private void startShowActivity(){
        Intent intent = new Intent(getActivity(), QaShowActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
    
    private void loadQuestion(){
        if(ConnectionApi.isConnection(getActivity()))
            new LoadQuestion().execute();
    }
    
    private class LoadQuestion extends AsyncTask<Void, Void, Void>{
        private String content;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element contentElement = document.select("div.content").first();

                /*
                 * http://stackoverflow.com/questions/3961589/android-webview-and-loaddata If you now how solve this problem an another way
                 * please sumbit a patch.
                 */
                content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                if(preferences.isUseCSS())
                    content += "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/style.css\" />";
                content += contentElement.outerHtml();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled()){
                WebView webView = (WebView) getActivity().findViewById(R.id.content);
                if(webView != null){
                    webView.getSettings().setPluginsEnabled(true);
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    //webView.loadData(content, "text/html", "UTF-8");
                    webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", null); 
                }
            }
        }

    }
    
    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }
    
    public String getLink(){
        return link;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
}