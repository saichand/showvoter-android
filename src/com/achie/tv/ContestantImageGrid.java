/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.achie.tv;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//Copyright 2011 Google Inc. All Rights Reserved.

import org.json.JSONException;

import com.achie.tv.adapter.ContestantsAdapter;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.Contestant;
import com.achie.tv.model.TvShow;
import com.achie.tv.service.VoteResultService;
import com.achie.tv.util.JsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The main Activity which displays the grid of photos fetched from
 * Panoramio service.
 */
public class ContestantImageGrid extends Activity implements OnClickListener {
    private static final String DEFAULT_QUERY = "San Francisco";

    ImageManager mImageManager;

    private static boolean isSplashShown = false;

    private Context mContext;

    private TextView textView;

    private ProgressBar progressBar;

    private String query;

    private String selectedContestantName;
    
    private ArrayList<Contestant> mContestants;
    private ContestantsAdapter mAdapter;
    
    private boolean mIsLoadingItems = false;
    private static final String tag = "########### ContestantsActivity :  ";
    
    private boolean hasVoted = false;
    private TvShow mShow;
    
    private long mContestantId = -1;
    /**
     * Simple Dialog used to show the splash screen.
     */
    protected Dialog mSplashDialog;

    @Override
    protected void onDestroy() {
        super.onStop();
        mImageManager.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        mImageManager = ImageManager.getInstance(mContext);
        try {
            handleIntent(getIntent());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tvShowString = extras.getString("show", "");
            Log.v(tag, "show string " + tvShowString);
            query = tvShowString;
        }
        
        if (!isSplashShown) {
            setContentView(R.layout.splash_screen_c);
            isSplashShown = true;
            CountDownTimer timer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    initGridView();
                }
            }.start();
        } else {
            initGridView();
        }
    }

    private GridView gridView;

    private void initGridView() {
        setContentView(R.layout.image_grid_c);
        gridView = (GridView) findViewById(R.id.gridview_c);
        mShow = JsonUtil.getShow(query);
        
        mContestants = mShow.contestants;
        
        mAdapter = new ContestantsAdapter(
                ContestantImageGrid.this, 
                R.layout.image_grid_c, 
                R.id.c_name, 
                mContestants) {
            
        };
        gridView.setAdapter(mAdapter);
        progressBar = (ProgressBar) findViewById(R.id.a_progressbar);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Create an intent to show a particular item.
                Log.v(tag, "position " + position);
                Log.v(tag, "clicked on " + mContestants.get(position).toJSONString());
                if (!hasVoted) {
                    mContestantId = mContestants.get(position).contestantId;
                    selectedContestantName = mContestants.get(position).name;
                    new SubmitVoteTask().execute("");
                }
            }
        });
        gridView.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            public void onChildViewAdded(View parent, View child) {
                progressBar.setVisibility(View.INVISIBLE);
                ((ViewGroup) parent).getChildAt(0).setSelected(true);
            }

            public void onChildViewRemoved(View parent, View child) {
            }
        });

        textView = (TextView) findViewById(R.id.show_name);
        textView.setText(mShow.showName);
        //VoteTvLeftNavService.getLeftNavBar(this);
        gridView.requestFocus();
    }

    public void onClick(View view) {
        onSearchRequested();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        progressBar.setVisibility(View.VISIBLE);
        try {
            handleIntent(intent);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        initGridView();
    }

    private void handleIntent(Intent intent) throws IOException, URISyntaxException, JSONException {
        /*if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        } else {
            query = intent.getStringExtra("query");
        }

        if (query == null || query.isEmpty()) {
            query = DEFAULT_QUERY;
        }*/
        // Start downloading
        //mImageManager.load(query);
        initGridView();
    }
    
    private class SubmitVoteTask extends AsyncTask<String, Integer, String> {
        private static final int PROGRESS_START_SEARCH = 0;
        private String resultString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            publishProgress(PROGRESS_START_SEARCH);
            if (!hasVoted) {
                Toast.makeText(ContestantImageGrid.this, "Casting Vote", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ContestantImageGrid.this, "Multiple votes are not allowed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ContestantImageGrid.this, "Vote Casted: " + resultString, Toast.LENGTH_SHORT).show();
            Intent resultService = new Intent(ContestantImageGrid.this, VoteResultService.class);
            Bundle bundle = new Bundle();
            bundle.putLong("showId", mShow.showId);
            bundle.putLong("contestantId", mContestantId);
            bundle.putString("selectedContestantName", selectedContestantName);
            resultService.putExtras(bundle);
            startService(resultService);
        }
        
        @Override
        protected void onCancelled() {
            super.onCancelled();
            hasVoted = false;
        }
    }
}
