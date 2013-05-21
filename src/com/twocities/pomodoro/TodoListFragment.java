package com.twocities.pomodoro;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.twocities.pomodoro.adapters.TodoCursorAdapter;
import com.twocities.pomodoro.data.Task;
import com.twocities.pomodoro.provider.TaskConstract;

public class TodoListFragment extends SwipeListFragment implements OnItemClickListener, OnItemLongClickListener,
		LoaderCallbacks<Cursor> {
	protected Object mActionMode;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		TodoCursorAdapter adapter = new TodoCursorAdapter(getActivity(), R.layout.layout_swipe_todo_item, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		getListView().setFastScrollEnabled(true);
		getListView().setOnItemClickListener(this);
		setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);
		final ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

		    @Override
		    public void onItemCheckedStateChanged(ActionMode mode, int position,
		                                          long id, boolean checked) {
		        // Here you can do something when items are selected/de-selected,
		        // such as update the title in the CAB
//		    	listView.setItemChecked(position, checked);
		    }

		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        // Respond to clicks on the actions in the CAB
		        switch (item.getItemId()) {
		            default:
		                return false;
		        }
		    }

		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate the menu for the CAB
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.task_view, menu);
		        return true;
		    }

		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		        // Here you can make any necessary updates to the activity when
		        // the CAB is removed. By default, selected items are deselected/unchecked.
		    }

		    @Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        // Here you can perform updates to the CAB due to
		        // an invalidate() request
		        return false;
		    }
		});
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Activity home = getActivity();
		if (home instanceof HomeActivity) {
			TaskFragment fragment = new TaskFragment();
			if (parent.getAdapter() instanceof TodoCursorAdapter) {
				TodoCursorAdapter adapter = (TodoCursorAdapter) parent
						.getAdapter();
				Cursor cursor = (Cursor) adapter.getItem(position);
				Task task = Task.CreateFromCursor(cursor);
				if (task == null) {
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putParcelable(Task.EXTRA_TASK_DATA, task);
				fragment.setArguments(bundle);
				((HomeActivity) home).switchContent(fragment, true);
			}
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
		        getUri(), 
		        getProjection(),
		        getSelection(),
		        getSelectionArgs(),
		        getSortOrder());
		    return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		getListAdapter().swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		getListAdapter().swapCursor(null);
	}
	
	@Override
	public void onDismiss(ListView listView, int[] reverseSortedPositions) {
		if (getListAdapter() instanceof TodoCursorAdapter) {
			TodoCursorAdapter adapter = (TodoCursorAdapter) getListAdapter();
			for (int position : reverseSortedPositions) {
				long id = adapter.getItemId(position);
				ContentValues values = new ContentValues();
				values.put(TaskConstract.Columns.FLAG_DONE, 1);
				Uri uri = ContentUris.withAppendedId(TaskConstract.CONTENT_ID_URI_BASE,
						id);
				getActivity().getContentResolver()
						.update(uri, values, null, null);
			}
			adapter.notifyDataSetChanged();
		}

	}
	
	protected Uri getUri() {
		return TaskConstract.CONTENT_URI;
	}
	
	protected String[] getProjection() {
		return null;
	}
	
	protected String getSelection() {
		return null;
	}
	
	protected String[] getSelectionArgs() {
		return null;
	}
	
	protected String getSortOrder() {
		return null;
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
        return false;
	}

}