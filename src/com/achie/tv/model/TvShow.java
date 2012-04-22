package com.achie.tv.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.achie.tv.C;

public class TvShow {

	public long inAppId;
	public String showName;
	public long startTime;
	public long endTime;
	public String seasonInformation;
	public String episodeInformation;
	public String imageUrl;
	public String description;
	public String channel;
	public long showId;
	
	public ArrayList<Contestant> contestants;
	
	private static final String tag = "########## TvShow : ";
	
	public String toJSONString() {
		JSONObject tvShowObject = new JSONObject();
		
		try {
			tvShowObject.putOpt(C.jsonKeys.IN_APP_ID, inAppId);
			tvShowObject.putOpt(C.jsonKeys.SHOW_NAME, showName);
			tvShowObject.putOpt(C.jsonKeys.START_TIME, startTime);
			tvShowObject.putOpt(C.jsonKeys.END_TIME, endTime);
			tvShowObject.putOpt(C.jsonKeys.SEASON_INFO, seasonInformation);
			tvShowObject.putOpt(C.jsonKeys.EPISODE_INFO, episodeInformation);
			tvShowObject.putOpt(C.jsonKeys.IMAGE_URL, imageUrl);
			tvShowObject.putOpt(C.jsonKeys.DESCRIPTION, description);
			tvShowObject.putOpt(C.jsonKeys.CHANNEL, channel);
			tvShowObject.putOpt(C.jsonKeys.SHOW_ID, showId);
			
			JSONArray contestantsArray = new JSONArray();
			
			Log.v(tag, " at 1");
			if (contestants != null && contestants.size() > 0) {
				Log.v(tag, " at 2");
				for (Contestant c : contestants) {
					Log.v(tag, " at 3 ");
					if (c != null) {
						Log.v(tag, " at 4 " + c.name);
						Log.v(tag, "c " + c.toJSONString());
						contestantsArray.put(new JSONObject(c.toJSONString()));
					}
				}
			}
			
			tvShowObject.putOpt(C.jsonKeys.CONTESTANTS, contestantsArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.v(tag, "returning " + tvShowObject.toString());
		
		return tvShowObject.toString();
	}
}
