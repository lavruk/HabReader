/*
Copyright 2012 Andrey Zaytsev

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostsCommentsAdapter extends BaseAdapter{
    protected ArrayList<CommentsData> commentsDatas;
    protected Context context;

    public PostsCommentsAdapter(Context context, ArrayList<CommentsData> commentsDatas){
        this.commentsDatas = commentsDatas;
        this.context = context;
    }

    public int getCount(){
        return commentsDatas.size();
    }

    public CommentsData getItem(int position){
        return commentsDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final CommentsData commentsData = getItem(position);

        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.comments_list_row, null);
        }

        int level = commentsData.getLevel();
        
        TextView comment = (TextView)view.findViewById(R.id.comment);
        comment.setText(commentsData.getComment());
        
        if(level > 0){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            layoutParams.setMargins(0, 0, level * 10, 0);
            comment.setLayoutParams(layoutParams);
        }

        return view;
    }

}