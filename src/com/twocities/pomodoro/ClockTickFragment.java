package com.twocities.pomodoro;

import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.twocities.pomodoro.data.PomodoroClock;
import com.twocities.pomodoro.provider.ClockProvider;
import com.twocities.pomodoro.widget.DigitalClock;

public class ClockTickFragment extends Fragment implements
		LoaderCallbacks<Cursor>, DigitalClock.onClockTickerListener {

	private DigitalClock mContentView;
	private Button mClockController;

	private int taskId;
	private String mSelection = "WHERE task_id == ? AND clock_state == ?";
	private long mClockLength = PomodoroClock.DEFAULT_LENGTH;
	private Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_clock_tick, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mClockController = (Button) view
				.findViewById(R.id.button_clock_control);
		mClockController.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clockController();
			}
		});
		mContentView = (DigitalClock) view.findViewById(R.id.digital_clock);
		mContentView.setOnClockTickerListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		taskId = getArguments().getInt("task_id");
		// start load manager
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		int running = PomodoroClock.Status.RUNNING;
		CursorLoader cursorLoader = new CursorLoader(
				getActivity(),
				PomodoroClock.Columns.CONTENT_URI,
				null,
				mSelection,
				new String[] { String.valueOf(taskId), String.valueOf(running) },
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data != null) {
			// there is a clock running.
			PomodoroClock clock = new PomodoroClock(data);
			mContentView.start(clock);
		} else {
			mContentView.reset(mClockLength);
		}
		View parent = (View) mContentView.getParent();
		parent.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private void clockController() {
		if (mContentView.isRuning()) {
			// stop current running clock.
			mContentView.stop();
		} else {
			// start a new clock.
			mContentView.start(new PomodoroClock(mClockLength, taskId));
		}
	}

	@Override
	public void onClockStart() {
		mClockController.setText(R.string.stop);
	}

	@Override
	public void onClockCancel() {
		mClockController.setText(R.string.start);
		mHandler.post(new UpdateRunnable(mContentView.getClock()));
	}

	@Override
	public void onClockFinish() {
		mClockController.setText(R.string.start);
		mHandler.post(new UpdateRunnable(mContentView.getClock()));
	}

	@Override
	public void onClockTick(long millisUntilFinished) {

	}

	private class UpdateRunnable implements Runnable {
		private PomodoroClock clock;

		public UpdateRunnable(PomodoroClock clock) {
			this.clock = clock;
		}

		@Override
		public void run() {
			// insert or update.
			getActivity().getContentResolver().insert(
					ClockProvider.CONTENT_URI, clock.toContentValues());
		}
	};
}