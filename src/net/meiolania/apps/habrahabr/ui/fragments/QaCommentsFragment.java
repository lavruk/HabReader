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

package net.meiolania.apps.habrahabr.ui.fragments;

import java.io.IOException;
import java.util.ArrayList;

import net.meiolania.apps.habrahabr.adapters.CommentsAdapter;
import net.meiolania.apps.habrahabr.api.ConnectionApi;
import net.meiolania.apps.habrahabr.data.CommentsData;
import android.os.AsyncTask;
import android.os.Bundle;

public class QaCommentsFragment extends ApplicationListFragment{
    protected ArrayList<CommentsData> commentsDataList;
    protected String link;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        
        if(link != null && !link.isEmpty())
            loadComments();
    }

    private void loadComments(){
        if(ConnectionApi.isConnection(getActivity()))
            new LoadComments().execute();
    }

    private class LoadComments extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params){
            try{
                commentsDataList = getApi().getQaCommentsApi().getComments(link);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(!isCancelled())
                setListAdapter(new CommentsAdapter(getActivity(), commentsDataList));
        }

    }

    public void setLink(String link){
        this.link = link;
    }

}