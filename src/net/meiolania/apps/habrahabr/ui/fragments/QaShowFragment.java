package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;

import net.meiolania.apps.habrahabr.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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
        
        View view = inflater.inflate(R.layout.qa_show_fragment, container, false);
        
        if(link != null)
            loadQuestion();
        
        if(isFullView){
            Button showComments = (Button)view.findViewById(R.id.comments);
            showComments.setVisibility(View.GONE);
        }else{
            Button showComments = (Button)view.findViewById(R.id.comments);
            showComments.setOnClickListener(new OnClickListener(){
                public void onClick(View v){
                    //startCommentsActivity();
                }
            });
        }
        
        return view;
    }
    
    private void loadQuestion(){
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
                webView.getSettings().setPluginsEnabled(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setBuiltInZoomControls(true);
                //webView.loadData(content, "text/html", "UTF-8");
                webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", null);
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