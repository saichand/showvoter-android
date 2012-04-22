package com.achie.tv;

import com.achie.tv.adapter.TvShowsAdapter;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.util.JsonUtil;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RealityShowActivity extends Activity {
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.tvshows);
        VoteTvLeftNavService.getLeftNavBar(this);
    }

    public void onClick(View view) {
        onSearchRequested();
    }

    static ListView shows;

    public static class ShowsFragment extends Fragment {

        int mCurCheckPosition = 0;

        int mShownCheckPosition = 0;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Intent intent = new Intent();
            intent.setClass(mContext, ShowImageGrid.class);
            mContext.startActivity(intent);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("curChoice", mCurCheckPosition);
        }
    }
}
