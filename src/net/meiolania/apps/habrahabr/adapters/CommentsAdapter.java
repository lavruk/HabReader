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

package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CommentsData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<CommentsData> commentsDataList;

    public CommentsAdapter(Context context, ArrayList<CommentsData> commentsDataList){
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
            view = layoutInflater.inflate(R.layout.comments_list_row, null);
        }
        
        TextView message = (TextView)view.findViewById(R.id.message);
        TextView author = (TextView)view.findViewById(R.id.author);
        TextView score = (TextView)view.findViewById(R.id.score);
        
        message.setText(commentsData.getText());
        author.setText(commentsData.getAuthor());
        score.setText(String.valueOf(commentsData.getScore()));
        
        if(commentsData.getScore() < 0)
            score.setTextColor(context.getResources().getColor(R.color.score_red));
        else if(commentsData.getScore() > 0)
            score.setTextColor(context.getResources().getColor(R.color.score_green));
        else
            score.setTextColor(context.getResources().getColor(R.color.score_gray));

        return view;
    }

}