package com.achie.tv;

import java.util.ArrayList;

import com.achie.tv.adapter.ContestantsAdapter;
import com.achie.tv.adapter.TvShowsAdapter;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.Contestant;
import com.achie.tv.model.TvShow;
import com.achie.tv.util.JsonUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ContestantsActivity extends Activity {

	private ListView mList;
	
	private ArrayList<Contestant> mContestants;
	private ContestantsAdapter mAdapter;
	
	private boolean mIsLoadingItems = false;
	private static final String tag = "########### ContestantsActivity :  ";
	
	private boolean hasVoted = false;
	private TvShow mShow;
	
	private long mContestantId = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	String tvShowString = extras.getString("show", "");
        	Log.v(tag, "show string " + tvShowString);
        	mShow = JsonUtil.getShow(tvShowString);
        	
        	mContestants = mShow.contestants;
        	
        	mAdapter = new ContestantsAdapter(
    				ContestantsActivity.this, 
    				R.layout.list_item_image_two_rows_c, 
    				R.id.c_name, 
    				mContestants) {
    			
    		};
    		mList = (ListView) findViewById(R.id.lv_shows);
    		mList.setAdapter(mAdapter);
    		
    		mList.setOnItemClickListener(onItemClickListener);
        }
        
        
    }
    
	public void onStart() {
		super.onStart();
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			Log.v(tag, "position " + position);
			Log.v(tag, "clicked on " + mContestants.get(position).toJSONString());
			if (!hasVoted) {
				mContestantId = mContestants.get(position).contestantId;
				new SubmitVoteTask().execute("");
			}
		}
	};
	
	private class SubmitVoteTask extends AsyncTask<String, Integer, String> {
		private static final int PROGRESS_START_SEARCH = 0;
		private String resultString = null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			publishProgress(PROGRESS_START_SEARCH);
			if (!hasVoted) {
				Toast.makeText(ContestantsActivity.this, "Casting Vote", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ContestantsActivity.this, "Multiple votes are not allowed", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected String doInBackground(String... params) {
			if (!hasVoted) {
				hasVoted = true;
				resultString = TvHttpHandler.postVote(mShow.showId, mContestantId);
			}
			else {
			    
			}
			return null;
		}
		
		@Override
		protected synchronized void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			switch (values[0]) {
			case PROGRESS_START_SEARCH:
				break;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(ContestantsActivity.this, "Vote Casted: " + resultString, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			hasVoted = false;
		}
	}
	
}