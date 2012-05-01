package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CompaniesData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CompaniesAdapter extends BaseAdapter{
    protected ArrayList<CompaniesData> companiesDatas;
    protected Context context;
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public CompaniesAdapter(Context context, ArrayList<CompaniesData> companiesDatas){
        this.context = context;
        this.companiesDatas = companiesDatas;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                                                     .memoryCacheSize(3000000)
                                                     .discCacheSize(50000000)
                                                     .httpReadTimeout(5000)
                                                     .defaultDisplayImageOptions(options)
                                                     .build();
        this.imageLoader.init(configuration);
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
        
        ImageView icon = (ImageView) view.findViewById(R.id.company_icon);
        imageLoader.displayImage(companiesData.getIcon(), icon, DisplayImageOptions.createSimple());

        return view;
    }

}