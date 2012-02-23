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

package net.meiolania.apps.habrahabr.ui.people;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<PeopleData> peopleDataList;

    public PeopleAdapter(Context context, ArrayList<PeopleData> peopleDataList){
        this.context = context;
        this.peopleDataList = peopleDataList;
    }

    public int getCount(){
        return peopleDataList.size();
    }

    public PeopleData getItem(int position){
        return peopleDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        PeopleData userData = peopleDataList.get(position);

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.people_list_row, null);
        }

        TextView name = (TextView) view.findViewById(R.id.list_name);
        name.setText(userData.getName());

        return view;
    }

}