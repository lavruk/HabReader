package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PostsAdapter extends BaseAdapter{
    protected ArrayList<PostsData> postsDatas;
    protected Context context;
    
    public PostsAdapter(Context context, ArrayList<PostsData> postsDatas){
        this.context = context;
        this.postsDatas = postsDatas;
    }
    
    public int getCount(){
        return postsDatas.size();
    }

    public PostsData getItem(int position){
        return postsDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final PostsData postsData = getItem(position);
        
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.posts_list_row, null);
        }
        
        TextView title = (TextView) view.findViewById(R.id.post_title);
        title.setText(postsData.getTitle());
        
        return view;
    }
    
}