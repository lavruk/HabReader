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
    private Context context;
    private ArrayList<PostsData> postsDataList;

    public PostsAdapter(Context context, ArrayList<PostsData> postsDataList){
        this.context = context;
        this.postsDataList = postsDataList;
    }

    public int getCount(){
        return postsDataList.size();
    }

    public PostsData getItem(int position){
        return postsDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        PostsData postsData = postsDataList.get(position);

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.posts_list_row, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(postsData.getTitle());
        
        TextView blog = (TextView)view.findViewById(R.id.blog);
        blog.setText(postsData.getBlog());

        return view;
    }

}