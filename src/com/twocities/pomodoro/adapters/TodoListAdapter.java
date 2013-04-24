package com.twocities.pomodoro.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.twocities.pomodoro.R;
import com.twocities.pomodoro.data.Tasks;

public class TodoListAdapter extends BaseAdapter {
	private List<Tasks> mList;
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	public TodoListAdapter(Context context, LayoutInflater inflater, List<Tasks> list) {
		this.mContext = context;
		this.mInflater = inflater;
		this.mList = list;
		
	}

	@Override
	public int getCount() {
		return this.mList.size();
	}

	@Override
	public Tasks getItem(int position) {
		return this.mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void remove(int position) {
		if(position < 0 || position >= getCount()) {
			return;
		}
		mList.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Tasks item = getItem(position);
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_swipe_todo_item, null);
			holder.mTitleView = (TextView) convertView.findViewById(R.id.text_todo_title);
			holder.mDescriptionView = (TextView) convertView.findViewById(R.id.text_todo_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTitleView.setText(item.title);
		holder.mDescriptionView.setText(item.description);
		
		return convertView;
	}
	
	private class ViewHolder {
		private TextView mTitleView;
		private TextView mDescriptionView;
	}
	

	
}