package net.meiolania.apps.habrahabr.fragments;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CompaniesFullData;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class CompaniesShowFragment extends SherlockFragment{
    protected String url;
    
    public CompaniesShowFragment(){}
    
    public CompaniesShowFragment(String url){
        this.url = url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getUrl(){
        return url;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        loadInfo();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.companies_show_activity, container, false);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.companies_show_activity, menu);
    }
    
    protected void loadInfo(){
        new LoadCompany().execute();
    }
    
    protected final class LoadCompany extends AsyncTask<Void, Void, CompaniesFullData>{
        private ProgressDialog progressDialog;
        
        @Override
        protected CompaniesFullData doInBackground(Void... params){
            
            return null;
        }
        
        @Override
        protected void onPreExecute(){
            
        }
        
        @Override
        protected void onPostExecute(CompaniesFullData result){
            
        }
        
    }
    
}