package com.twocities.pomodoro;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

import com.twocities.pomodoro.Utils.Log;
import com.twocities.pomodoro.adapters.TodoListAdapter;
import com.twocities.pomodoro.widget.swipelistview.SwipeListView;
import com.twocities.pomodoro.widget.swipelistview.SwipeListViewListener;

public class TodoListFragment extends Fragment implements
		SwipeListViewListener, OnItemClickListener, OnItemLongClickListener,
		LoaderCallbacks<Cursor>,
		OnItemSelectedListener {
	private TodoListAdapter mAdapter;
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
	
	public void setAdapter(BaseAdapter adapter) {
		mAdapter = (TodoListAdapter) adapter;
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
	}

	@Override
	public void onOpened(int position, boolean toRight) {
		Log.v("onOpened");
	}

	@Override
	public void onClosed(int position, boolean fromRight) {
		Log.v("onClosed");

	}

	@Override
	public void onListChanged() {
		Log.v("onListChanged");

	}

	@Override
	public void onMove(int position, float x) {

	}

	@Override
	public void onStartOpen(int position, int action, boolean right) {
		Log.v("onStartOpen");

	}

	@Override
	public void onStartClose(int position, boolean right) {

	}

	@Override
	public void onClickFrontView(int position) {

	}

	@Override
	public void onClickBackView(int position) {

	}

	@Override
	public void onDismiss(int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mAdapter.remove(position);
        }
        mAdapter.notifyDataSetChanged();
	}

	@Override
	public int onChangeSwipeMode(int position) {
		return SwipeListView.SWIPE_MODE_DEFAULT;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
}