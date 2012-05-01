package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PeopleData;
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

public class PeopleAdapter extends BaseAdapter{
    protected ArrayList<PeopleData> peopleDatas;
    protected Context context;
    protected DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public PeopleAdapter(Context context, ArrayList<PeopleData> peopleDatas){
        this.context = context;
        this.peopleDatas = peopleDatas;
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
        
        ImageView avatar = (ImageView) view.findViewById(R.id.people_avatar);
        imageLoader.displayImage(peopleData.getAvatar(), avatar);

        return view;
    }

}