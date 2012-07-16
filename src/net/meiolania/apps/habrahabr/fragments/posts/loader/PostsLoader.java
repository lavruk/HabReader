package net.meiolania.apps.habrahabr.fragments.posts.loader;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.data.PostsData;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class PostsLoader extends AsyncTaskLoader<ArrayList<PostsData>>{
	private String url;
	
	public PostsLoader(Context context, String url){
		super(context);
		
		this.url = url;
	}

	@Override
	public ArrayList<PostsData> loadInBackground(){
		return null;
	}

}