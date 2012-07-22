/*
Copyright 2012 Andrey Zaytsev

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

package net.meiolania.apps.habrahabr.adapters;

import java.util.ArrayList;

import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.CommentsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter{
	public final static int MARGIN = 10;
	private ArrayList<CommentsData> comments;
	private Context context;

	public CommentsAdapter(Context context, ArrayList<CommentsData> comments){
		this.comments = comments;
		this.context = context;
	}

	public int getCount(){
		return comments.size();
	}

	public CommentsData getItem(int position){
		return comments.get(position);
	}

	public long getItemId(int position){
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		CommentsData data = getItem(position);

		View view = convertView;
		if(view == null){
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.comments_list_row, null);
		}

		int level = data.getLevel();

		TextView comment = (TextView) view.findViewById(R.id.comment);
		comment.setText(data.getComment());
		
		if(level > 0){
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
																				   LinearLayout.LayoutParams.FILL_PARENT);
			layoutParams.setMargins(level * MARGIN, 0, 0, 0);
			comment.setLayoutParams(layoutParams);
		}

		return view;
	}

}