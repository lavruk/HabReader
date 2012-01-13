package net.meiolania.apps.habrahabr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAdapter extends SQLiteOpenHelper{
    public final static String DB_NAME = "habrahabr";
    public final static int DB_VERSION = 1;
    
    public DatabaseAdapter(Context context, String name){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        
    }

}