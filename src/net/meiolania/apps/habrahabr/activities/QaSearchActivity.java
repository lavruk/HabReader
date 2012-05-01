package net.meiolania.apps.habrahabr.activities;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.fragments.SearchQaFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class QaSearchActivity extends AbstractionActivity{
    public final static String EXTRA_QUERY = "query";
    private String query;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        loadExtras();
        showActionBar();
        loadSearchedQuestions();
    }
    
    private void loadExtras(){
        query = getIntent().getStringExtra(EXTRA_QUERY);
    }
    
    private void showActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.qa_search);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    private void loadSearchedQuestions(){
        SearchQaFragment searchQaFragment = new SearchQaFragment(query);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, searchQaFragment);
        fragmentTransaction.commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, QaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
}