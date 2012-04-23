package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PeopleData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter{
    protected ArrayList<PeopleData> peopleDatas;
    protected Context context;

    public PeopleAdapter(Context context, ArrayList<PeopleData> peopleDatas){
        this.context = context;
        this.peopleDatas = peopleDatas;
    }

    public int getCount(){
        return peopleDatas.size();
    }

    public PeopleData getItem(int position){
        return peopleDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        PeopleData peopleData = getItem(position);

        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.people_list_row, null);
        }

        TextView title = (TextView) view.findViewById(R.id.people_title);
        title.setText(peopleData.getName());

        return view;
    }

}