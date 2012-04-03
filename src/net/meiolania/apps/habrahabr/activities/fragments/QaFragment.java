package net.meiolania.apps.habrahabr.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class QaFragment extends SherlockFragment{
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Toast.makeText(getActivity(), "Q&A", Toast.LENGTH_SHORT).show();
        TextView textView = new TextView(getActivity());
        textView.setText("Q&A");
        return textView;
    }
    
}