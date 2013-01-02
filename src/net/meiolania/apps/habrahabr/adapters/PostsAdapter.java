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

import net.meiolania.apps.habrahabr.Preferences;
import net.meiolania.apps.habrahabr.R;
import net.meiolania.apps.habrahabr.data.PostsData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PostsAdapter extends BaseAdapter {
	private ArrayList<PostsData> posts;
	private Context context;
	private boolean additionalLayout = false;

	public PostsAdapter(Context context, ArrayList<PostsData> posts) {
		this.context = context;
		this.posts = posts;

		Preferences preferences = Preferences.getInstance(context);
		this.additionalLayout = preferences.getAdditionalPosts();
	}

	public int getCount() {
		return posts.size();
	}

	public PostsData getItem(int position) {
		return posts.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		PostsData data = getItem(position);

		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.posts_list_row, null);
		}

		TextView title = (TextView) view.findViewById(R.id.post_title);
		title.setText(data.getTitle());

		TextView hubs = (TextView) view.findViewById(R.id.post_hubs);
		TextView author = (TextView) view.findViewById(R.id.post_author);
		TextView date = (TextView) view.findViewById(R.id.post_date);
		TextView score = (TextView) view.findViewById(R.id.post_score);

		RelativeLayout postInfo = (RelativeLayout) view
				.findViewById(R.id.post_info);

		if (additionalLayout) {
			hubs.setText(data.getHubs());
			author.setText(data.getAuthor());
			date.setText(data.getDate());
			if (data.getScore().trim().equals("—")) {
				score.setVisibility(View.GONE);
				// score.setText(data.getScore());
			} else {
				score.setVisibility(View.VISIBLE);
				String rate = data.getScore().replace("+", "")
						.replace("–", "-");

				if (Integer.parseInt(rate) > 0)
					score.setTextColor(context.getResources().getColor(
							R.color.rating_positive));
				else if (Integer.parseInt(rate) < 0)
					score.setTextColor(context.getResources().getColor(
							R.color.rating_negative));

				score.setText(data.getScore());
			}
		} else {
			hubs.setVisibility(View.GONE);
			postInfo.setVisibility(View.GONE);
		}

		return view;
	}

}