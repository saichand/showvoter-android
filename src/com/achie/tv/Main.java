package com.achie.tv;

import java.util.ArrayList;

import com.achie.tv.adapter.TvShowsAdapter;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.TvShow;
import com.achie.tv.util.JsonUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Main extends Activity {

	private ListView mList;
	
	private ArrayList<TvShow> mTvShows;
	private TvShowsAdapter mAdapter;

	private ShowTvShowsTask mSearchTask;
	
	
	private static final String tag = "########### Main :  ";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mList = (ListView) findViewById(R.id.lv_shows);
        
        initializeEmptyListView();
        
		startSearch();
    }
	
	private void initializeEmptyListView() {
		mTvShows = new ArrayList<TvShow>();
		mAdapter = new TvShowsAdapter(
				Main.this, 
				R.layout.list_item_image_two_rows, 
				R.id.tv_title, 
				mTvShows) {
			
		};
		
		mList.setAdapter(mAdapter);
		
		mList.setOnItemClickListener(onItemClickListener);
	}
	
	public void onStart() {
		super.onStart();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			Log.v(tag, "clicked on position : " + position + " ; , id : " + id);
			Log.v(tag, "item " + mTvShows.get(position).toJSONString());
			Intent intent = new Intent(Main.this, ContestantsActivity.class);
			intent.putExtra("show", mTvShows.get(position).toJSONString());
			startActivity(intent);
		}
	};
	
	private void startSearch() {
		if (mSearchTask != null) {
			mSearchTask.cancel(true);
		}
		
		mTvShows.clear();
		mAdapter.notifyDataSetChanged();
		
		mSearchTask = new ShowTvShowsTask(); 
		mSearchTask.execute("");
	}
    
	private class ShowTvShowsTask extends AsyncTask<String, Integer, Void> {
		
		private static final int PROGRESS_START_SEARCH = 0;
		private static final int PROGRESS_REFRESH_RESULTS = 1;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			publishProgress(PROGRESS_START_SEARCH);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			if (!isCancelled())
				showTvShows("", 0, 0);
			return null;
		}
		
		@Override
		protected synchronized void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			switch (values[0]) {
			case PROGRESS_START_SEARCH:
				break;
			case PROGRESS_REFRESH_RESULTS:
				mAdapter = new TvShowsAdapter(
						Main.this, 
						R.layout.list_item_image_two_rows, 
						R.id.tv_title, 
						mTvShows) {
					
				};
				
				mList.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				break;
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
		
		private synchronized void showTvShows(String searchString, int limit, int offset) {
			String showsJson = TvHttpHandler.getShows();
			
			Log.v(tag, " json is : " + showsJson);
			
			if (!isCancelled()) { // do not update the list if the task has been cancelled.
				mTvShows = JsonUtil.getShows(showsJson);
				
				if(mTvShows != null)
				Log.v(tag, "mtvshows size : " + mTvShows.size());
				publishProgress(PROGRESS_REFRESH_RESULTS);
			}
		}
	}
}