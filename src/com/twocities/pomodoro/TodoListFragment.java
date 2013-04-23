package com.twocities.pomodoro;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.widget.swipelistview.SwipeListView;
import com.twocities.pomodoro.widget.swipelistview.SwipeListViewListener;

public class TodoListFragment extends Fragment implements
		SwipeListViewListener, OnItemClickListener, OnItemLongClickListener,
		OnItemSelectedListener {
	private ListAdapter mAdapter;
	private SwipeListView mListView;
	private boolean mListShown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.swipe_todo_list, null);
		mListView = (SwipeListView) v.findViewById(R.id.swipe_todo_list);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		mListView.setAdapter(adapter);
		setListShown();
	}
	
	private void setListShown() {
		ensureList();
		mListView.setVisibility(View.VISIBLE);
	}
	
	private void ensureList() {
		mListView.setSwipeListViewListener(this);
		mListView.setSwipeOpenOnLongPress(false);
//		mListView.setOnItemClickListener(this);
//		mListView.setOnItemLongClickListener(this);
//		mListView.setOnItemSelectedListener(this);
	}

	public static final String[] TODOLIST = {
			"Add Setting Screen to DidaClient", "fix bug #34512",
			"Change ListView background", "try to add ActionBarSherlock",
			"write blog", "drink coffee", "Add Setting Screen to DidaClient",
			"fix bug #34512", "Change ListView background",
			"try to add ActionBarSherlock", "write blog", "drink coffee",
			"Add Setting Screen to DidaClient", "fix bug #34512",
			"Change ListView background", "try to add ActionBarSherlock",
			"write blog", "drink coffee", "Add Setting Screen to DidaClient",
			"fix bug #34512", "Change ListView background",
			"try to add ActionBarSherlock", "write blog", "drink coffee",
			"Add Setting Screen to DidaClient", "fix bug #34512",
			"Change ListView background", "try to add ActionBarSherlock",
			"write blog", "drink coffee" };

	@Override
	public void onOpened(int position, boolean toRight) {
		Log.v("onOpened");
	}

	@Override
	public void onClosed(int position, boolean fromRight) {
		// TODO Auto-generated method stub
		Log.v("onClosed");

	}

	@Override
	public void onListChanged() {
		// TODO Auto-generated method stub
		Log.v("onListChanged");

	}

	@Override
	public void onMove(int position, float x) {
		// TODO Auto-generated method stub
		Log.v("onMove");

	}

	@Override
	public void onStartOpen(int position, int action, boolean right) {
		// TODO Auto-generated method stub
		Log.v("onStartOpen");

	}

	@Override
	public void onStartClose(int position, boolean right) {
		// TODO Auto-generated method stub
		Log.v("onStartClose");

	}

	@Override
	public void onClickFrontView(int position) {
		// TODO Auto-generated method stub
		Log.v("onClickFrontView");

	}

	@Override
	public void onClickBackView(int position) {
		// TODO Auto-generated method stub
		Log.v("onClickBackView");

	}

	@Override
	public void onDismiss(int[] reverseSortedPositions) {
		// TODO Auto-generated method stub
		Log.v("onDismiss");

	}

	@Override
	public int onChangeSwipeMode(int position) {
		// TODO Auto-generated method stub
		return SwipeListView.SWIPE_MODE_DEFAULT;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}