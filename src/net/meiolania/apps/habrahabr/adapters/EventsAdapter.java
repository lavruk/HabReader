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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.EventsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter{
    protected ArrayList<EventsData> eventsDatas;
    protected Context context;
    protected boolean additionalLayout = false;
    
    public EventsAdapter(Context context, ArrayList<EventsData> eventsDatas){
        this.context = context;
        this.eventsDatas = eventsDatas;
        
        Preferences preferences = Preferences.getInstance(context);
        this.additionalLayout = preferences.getAdditionalEvents();
    }
    
    public int getCount(){
        return eventsDatas.size();
    }

    public EventsData getItem(int position){
        return eventsDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        EventsData eventsData = getItem(position);
        
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.events_list_row, null);
        }
        
        TextView title = (TextView)view.findViewById(R.id.event_title);
        title.setText(eventsData.getTitle());
        
        TextView text = (TextView)view.findViewById(R.id.event_text);
        if(additionalLayout)
            text.setText(eventsData.getText());
        else
            text.setVisibility(View.GONE);
        
        return view;
    }

}