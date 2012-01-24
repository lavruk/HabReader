package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.ui.activities.PostsShowActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class PostsShowFragment extends ApplicationFragment{
    private String link;
    private String title;
    private boolean isFullView = false;
    private LoadPost loadPost;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;
        
        View view = inflater.inflate(R.layout.posts_show_fragment, container, false);
        
        if(link != null)
            loadPost();
        
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
    
    private void startShowActivity(){
        Intent intent = new Intent(getActivity(), PostsShowActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
    
    public void setIsFullView(boolean isFullView){
        this.isFullView = isFullView;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setLink(String link){
        this.link = link;
    }
    
    public String getLink(){
        return link;
    }
    
    private void loadPost(){
        if(ConnectionApi.isConnection(getActivity())){
            loadPost = new LoadPost();
            loadPost.execute();
        }
    }

    private class LoadPost extends AsyncTask<Void, Void, Void>{
        private String content;

        @Override
        protected Void doInBackground(Void... params){
            try{
                Document document = Jsoup.connect(link).get();

                Element contentElement = document.select("div.content").first();
                Element titleElement = document.select("span.post_title").first();

                title = titleElement.text();

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
                    webView.getSettings().setPluginsEnabled(preferences.isEnableFlashPosts());
                    webView.getSettings().setSupportZoom(true);
                    webView.getSettings().setBuiltInZoomControls(true);
                    //webView.loadData(content, "text/html", "UTF-8");
                    webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
                }
            }
        }

    }
    
}