package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.Formatter;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.activities.PostsCommentsShow;
import net.meiolania.apps.habrahabr.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

public class PostsShowFragment extends ApplicationFragment{
    public final static String TAG = "Habrahabr.PostsShowFragment";
    private String link;
    private String title;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(container == null)
            return null;
        
        View view = inflater.inflate(R.layout.posts_show_fragment, container, false);
        
        if(link != null)
            loadPost();
        
        return view;
    }
    
    public void postsShowOnClick(View view){
        switch(view.getId()){
            case R.id.comments:
                startCommentsActivity();
                break;
        }
    }
    
    private class ShowCommentsAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_comments;
        }

        public void performAction(View view){
            startCommentsActivity();
        }

    }
    
    private void startCommentsActivity(){
        Intent intent = new Intent(getActivity(), PostsCommentsShow.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    private class ShareAction implements Action{

        public int getDrawable(){
            return R.drawable.actionbar_ic_share;
        }

        public void performAction(View view){
            createShareIntent();
        }

    }
    
    private void createShareIntent(){
        final Intent intent = new Intent(Intent.ACTION_SEND);
        final Formatter formatter = new Formatter();
        String shareText = formatter.format("%s - %s #HabraHabr #HabReader", title, link).toString();

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_link_post));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, getString(R.string.share)));
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
        new LoadPost().execute();
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