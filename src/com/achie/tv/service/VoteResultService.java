package com.achie.tv.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.achie.tv.C;
import com.achie.tv.http.TvHttpHandler;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class VoteResultService extends Service {

	
	private Looper mServiceLooper;
	  private VoteResultHandler mServiceHandler;

	  // Handler that receives messages from the thread
	  private final class VoteResultHandler extends Handler {
	      public VoteResultHandler(Looper looper) {
	          super(looper);
	      }
	      
	      @Override
	      public void handleMessage(Message msg) {
	    	  String resultData = msg.getData().getString("result");
	    	  Log.v("VoteResultService", "Result data: " + resultData);
	    	  Toast.makeText(getApplicationContext(), resultData, Toast.LENGTH_SHORT).show();
	    	  Log.v("VoteResultService", "Completed Toast Call");
	      }
	  }
	  
	  

	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",
	            android.os.Process.THREAD_PRIORITY_BACKGROUND);
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new VoteResultHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      
	      Bundle dataFromActivity = intent.getExtras();
	      long showId = dataFromActivity.getLong("showId");
	      long contestantId = dataFromActivity.getLong("contestantId");
	      String selectedContestantName = dataFromActivity.getString("selectedContestantName");
	      
	      Log.v("VoteResultService", "ShowId: " + showId + " ContestantId: " + contestantId);
	      
	      for (int i = 1; i <= 50; i++) {
	    	  new Thread(new ResultFetcher(showId, contestantId, selectedContestantName, i * 5000)).start(); 
	      }
	      
	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }
	  
	  private class ResultFetcher implements Runnable {
		  
		  private long showId;
		  private long contestantId;
		  private String contestantName;
		  
		  private long waitTime;
		  
		  public ResultFetcher(long showId, long contestantId, String selectedContestantName, long waitTime) {
			  this.showId = showId;
			  this.contestantId = contestantId;
			  this.contestantName = selectedContestantName;
			  this.waitTime = waitTime;
		  }

		@Override
		public void run() {
			synchronized (this) {
	    		  try {
					wait(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
			  
			  StringBuilder result = new StringBuilder("New Results for ");
	    	  result.append(contestantName);
	    	  
	    	  String cResultJson = TvHttpHandler.getResult(showId, contestantId);
	    	  if (cResultJson != null) {
	    		  try {
					JSONObject resultJsonObj = new JSONObject(cResultJson);
					long votes = resultJsonObj.optLong(C.jsonKeys.CONT_VOTES);
					String rank = resultJsonObj.optString(C.jsonKeys.CONT_RANK);
					result.append("\n Votes: ").append(votes);
					result.append("\n Rank: ").append(rank);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	  }
	    	  Message msg = mServiceHandler.obtainMessage();
	    	  Bundle resultBundle = new Bundle();
	    	  resultBundle.putString("result", result.toString());
	    	  msg.setData(resultBundle);
	    	  mServiceHandler.sendMessage(msg);
		}
		  
	  }
	  
	  @Override
	  public IBinder onBind(Intent intent) {
	      // We don't provide binding, so return null
	      return null;
	  }
	  
	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	  }
}
