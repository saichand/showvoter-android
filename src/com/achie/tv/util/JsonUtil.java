package com.achie.tv.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.achie.tv.C;
import com.achie.tv.model.Contestant;
import com.achie.tv.model.TvShow;

public class JsonUtil {
	
	public static Contestant getContestant(String contestantJson) {
		if (contestantJson == null || contestantJson.trim().length() == 0 || contestantJson.equalsIgnoreCase("null")) return null;
		
		Contestant contestant = new Contestant();
		try {
			JSONObject contestantObject = new JSONObject(contestantJson);
			contestant.name = contestantObject.optString(C.jsonKeys.CONT_NAME);
			contestant.contestantId = contestantObject.optLong(C.jsonKeys.CONT_ID);
			contestant.contestantInfo = contestantObject.optString(C.jsonKeys.CONT_INFO);
			//contestant.imageUrl = contestantObject.optString(C.jsonKeys.CONT_IMG_URL);
			//contestant.imageUrl = contestantObject.optString(C.jsonKeys.CONT_IMG_URL);
			contestant.imageUrl = contestantObject.optString(C.jsonKeys.CONT_IMG_URL);
			contestant.voteCount = contestantObject.optLong(C.jsonKeys.CONT_VOTES);
			contestant.rank = contestantObject.optString(C.jsonKeys.CONT_RANK);
			contestant.contestantCode= contestantObject.optString(C.jsonKeys.CONT_CODE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return contestant;
	}

	public static ArrayList<Contestant> getContestants(String contestantsJson) {
		if (contestantsJson == null || contestantsJson.trim().length() == 0 || contestantsJson.equalsIgnoreCase("null")) return null;
		ArrayList<Contestant> contestants = new ArrayList<Contestant>();

		JSONArray contestantsArray = null;
		
		try {
			contestantsArray = new JSONArray(contestantsJson);
			
			int length = contestantsArray.length();
			for (int i=0; i<length; i++) {
				JSONObject contestantJSONObject = contestantsArray.getJSONObject(i);
				
				Contestant contestant = getContestant(contestantJSONObject.toString());
				if (contestant != null) {
					contestant.inAppId = contestants.size() + 1;
					contestants.add(contestant);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return contestants;
	}
	
	public static TvShow getShow(String showJson) {
		if (showJson == null || showJson.trim().length() == 0 || showJson.equalsIgnoreCase("null")) return null;
		
		TvShow show = new TvShow();
		try {
			JSONObject showObject = new JSONObject(showJson);
			show.showName = showObject.optString(C.jsonKeys.SHOW_NAME);
			show.startTime = showObject.optLong(C.jsonKeys.START_TIME);
			show.endTime = showObject.optLong(C.jsonKeys.END_TIME);
			show.seasonInformation = showObject.optString(C.jsonKeys.SEASON_INFO);
			show.episodeInformation = showObject.optString(C.jsonKeys.EPISODE_INFO);
			show.imageUrl = showObject.optString(C.jsonKeys.IMAGE_URL);
			show.description = showObject.optString(C.jsonKeys.DESCRIPTION);
			show.channel = showObject.optString(C.jsonKeys.CHANNEL);
			show.showId = showObject.optLong(C.jsonKeys.SHOW_ID);
			show.contestants = new ArrayList<Contestant>();
			JSONArray contestantsArray = showObject.optJSONArray(C.jsonKeys.CONTESTANTS);
			if (contestantsArray != null) {
				for (int i=0; i<contestantsArray.length(); i++) {
					Contestant contestant = getContestant(contestantsArray.getJSONObject(i).toString());
					contestant.inAppId = show.contestants.size();
					show.contestants.add(contestant);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return show;
	}
	
	public static ArrayList<TvShow> getShows(String showsJson) {
		if (showsJson == null || showsJson.trim().length() == 0 || showsJson.equalsIgnoreCase("null")) return null;
		System.out.println("SHOWS JSON = " + showsJson);
		ArrayList<TvShow> shows = new ArrayList<TvShow>();
		JSONArray showsArray = null;
		
		try {
			showsArray = new JSONArray(showsJson);
			
			int length = showsArray.length();
			for (int i=0; i<length; i++) {
				JSONObject showJSONObject = showsArray.getJSONObject(i);
				
				TvShow show = getShow(showJSONObject.toString());
				if (show != null) {
					show.inAppId = shows.size() + 1;
					shows.add(show);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return shows;
	}
	
}