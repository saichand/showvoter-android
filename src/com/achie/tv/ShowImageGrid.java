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
import android.widget.ProgressBar;
import android.widget.TextView;
//Copyright 2011 Google Inc. All Rights Reserved.

import org.json.JSONException;

import com.achie.tv.adapter.TvShowsAdapter;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.TvShow;
import com.achie.tv.util.JsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main Activity which displays the grid of photos fetched from
 * Panoramio service.
 */
public class ShowImageGrid extends Activity implements OnClickListener {
    private static final String DEFAULT_QUERY = "";

    ImageManager mImageManager;

    private static boolean isSplashShown = false;

    private Context mContext;

    private TextView textView;

    private ProgressBar progressBar;

    private List<TvShow> mTvShows;
    
    private TvShowsAdapter mAdapter;

    private ShowTvShowsTask mSearchTask;
    
    private static final String tag = "########### Main :  ";
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
            startSearch();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        if (!isSplashShown) {
            setContentView(R.layout.splash_screen);
            isSplashShown = true;
            CountDownTimer timer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    //startSearch();
                    //initGridView();
                }
            }.start();
        } else {
            //initGridView();
            //startSearch();
        }
    }

    private GridView gridView;

    private void startSearch() {
        if (mSearchTask != null) {
            mSearchTask.cancel(true);
        }
        
        if (mTvShows != null) {
            mTvShows.clear();
        }
        
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        
        mSearchTask = new ShowTvShowsTask(); 
        mSearchTask.execute("");
    }
    
    private void initGridView() {
        setContentView(R.layout.image_grid);
        gridView = (GridView) findViewById(R.id.gridview);
        mAdapter = new TvShowsAdapter(mContext, R.layout.image_grid, R.id.show_header, mTvShows) {
        };
        //final ImageAdapter imageAdapter = new ImageAdapter(mContext);
        gridView.setAdapter(mAdapter);
        progressBar = (ProgressBar) findViewById(R.id.a_progressbar);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Create an intent to show a particular item.
                final Intent i = new Intent(ShowImageGrid.this, ContestantImageGrid.class);
                
                Log.v(tag, "clicked on position : " + position + " ; , id : " + id);
                Log.v(tag, "item " + mTvShows.get(position).toJSONString());
                i.putExtra("show", mTvShows.get(position).toJSONString());
                startActivity(i);
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

        textView = (TextView) findViewById(R.id.show_header);
        textView.setText("TV Shows");
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
        //startSearch();
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
                initGridView();
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
