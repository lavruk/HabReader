package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

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
    
    public EventsAdapter(Context context, ArrayList<EventsData> eventsDatas){
        this.context = context;
        this.eventsDatas = eventsDatas;
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
        
        return view;
    }

}