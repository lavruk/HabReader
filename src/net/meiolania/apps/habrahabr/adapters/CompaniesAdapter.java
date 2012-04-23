package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CompaniesData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CompaniesAdapter extends BaseAdapter{
    protected ArrayList<CompaniesData> companiesDatas;
    protected Context context;

    public CompaniesAdapter(Context context, ArrayList<CompaniesData> companiesDatas){
        this.context = context;
        this.companiesDatas = companiesDatas;
    }

    public int getCount(){
        return companiesDatas.size();
    }

    public CompaniesData getItem(int position){
        return companiesDatas.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        CompaniesData companiesData = getItem(position);

        View view = convertView;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.companies_list_row, null);
        }

        TextView title = (TextView) view.findViewById(R.id.company_title);
        title.setText(companiesData.getTitle());

        return view;
    }

}