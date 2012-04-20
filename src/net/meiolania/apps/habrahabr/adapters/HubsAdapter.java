package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.HubsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HubsAdapter extends BaseAdapter{
    protected ArrayList<HubsData> hubsDatas;
    protected Context context;

    public HubsAdapter(Context context, ArrayList<HubsData> hubsDatas){
        this.context = context;
        this.hubsDatas = hubsDatas;
    }

    public int getCount(){
        return hubsDatas.size();
    }

    public HubsData getItem(int position){
        return hubsDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final HubsData hubsData = getItem(position);
        
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.hubs_list_row, null);
        }
        
        TextView title = (TextView)view.findViewById(R.id.hub_title);
        title.setText(hubsData.getTitle());
        
        return view;
    }

}