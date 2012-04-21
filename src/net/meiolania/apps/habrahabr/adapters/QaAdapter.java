package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.QaData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QaAdapter extends BaseAdapter{
    protected ArrayList<QaData> qaDatas;
    protected Context context;
    
    public QaAdapter(Context context, ArrayList<QaData> qaDatas){
        this.context = context;
        this.qaDatas = qaDatas;
    }
    
    public int getCount(){
        return qaDatas.size();
    }

    public QaData getItem(int position){
        return qaDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final QaData qaData = getItem(position);
        
        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.qa_list_row, null);
        }
        
        TextView title = (TextView)view.findViewById(R.id.qa_title);
        title.setText(qaData.getTitle());
        
        return view;
    }

}