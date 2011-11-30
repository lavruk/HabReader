package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CommentsData;
import net.meiolania.apps.habrahabr.data.CompaniesData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsPostsAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<CommentsData> commentsDataList;

    public CommentsPostsAdapter(Context context, ArrayList<CommentsData> commentsDataList){
        this.context = context;
        this.commentsDataList = commentsDataList;
    }

    public int getCount(){
        return commentsDataList.size();
    }

    public CommentsData getItem(int position){
        return commentsDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        CommentsData commentsData = commentsDataList.get(position);

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.companies_list_row, null);
        }

        return view;
    }

}