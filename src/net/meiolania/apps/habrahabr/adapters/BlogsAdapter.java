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
import net.meiolania.apps.habrahabr.data.BlogsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlogsAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<BlogsData> blogsDataList;

    public BlogsAdapter(Context context, ArrayList<BlogsData> blogsDataList){
        this.context = context;
        this.blogsDataList = blogsDataList;
    }

    public int getCount(){
        return blogsDataList.size();
    }

    public BlogsData getItem(int position){
        return blogsDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        BlogsData blogsData = blogsDataList.get(position);

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.blogs_list_row, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(blogsData.getTitle());

        TextView statistics = (TextView) view.findViewById(R.id.statistics);
        statistics.setText(blogsData.getStatistics());

        return view;
    }

}