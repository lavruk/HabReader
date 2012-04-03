package net.meiolania.apps.habrahabr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PostsFragment extends AbstractPostsFragment{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Toast.makeText(getActivity(), "Posts", Toast.LENGTH_SHORT).show();
        TextView textView = new TextView(getActivity());
        textView.setText("Posts");
        return textView;
    }
    
    @Override
    public String getSectionUrl(){
        return "";
    }

}