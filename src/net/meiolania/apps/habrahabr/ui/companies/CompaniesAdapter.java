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

package net.meiolania.apps.habrahabr.ui.companies;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.Api;
import net.meiolania.apps.habrahabr.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CompaniesAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<CompaniesData> companiesDataList;

    public CompaniesAdapter(Context context, ArrayList<CompaniesData> companiesDataList){
        this.context = context;
        this.companiesDataList = companiesDataList;
    }

    public int getCount(){
        return companiesDataList.size();
    }

    public CompaniesData getItem(int position){
        return companiesDataList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        CompaniesData companiesData = companiesDataList.get(position);

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.companies_list_row, null);
        }

        TextView title = (TextView) view.findViewById(R.id.list_title);
        title.setText(companiesData.getTitle());
        
        ImageView logo = (ImageView) view.findViewById(R.id.list_logo);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.displayImage(Api.MAIN_PAGE + companiesData.getLogo(), logo);

        return view;
    }

}