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

import net.meiolania.apps.habrahabr.activities.PostsShow;
import net.meiolania.apps.habrahabr.data.PostsData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class PostsDashboardFragment extends PostsFragment{
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onListItemClick(ListView list, View view, int position, long id){
        PostsData postsData = postsDataList.get(position);

        Intent intent = new Intent(getActivity(), PostsShow.class);
        intent.putExtra("link", postsData.getLink());
        
        startActivity(intent);
    }
    
}