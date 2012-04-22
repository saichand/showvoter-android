package com.achie.tv.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.achie.tv.C;

public class Contestant {

	public long inAppId;
	public long contestantId;
	public String name;
	public String contestantInfo;
	public String imageUrl;
	public long voteCount;
	public String rank;
	
	public String toJSONString() {
		JSONObject contestantObject = new JSONObject();
		
		try {
			contestantObject.putOpt(C.jsonKeys.IN_APP_ID, inAppId);
			contestantObject.putOpt(C.jsonKeys.CONT_NAME, name);
			contestantObject.putOpt(C.jsonKeys.CONT_ID, contestantId);
			contestantObject.putOpt(C.jsonKeys.CONT_INFO, contestantInfo);
			contestantObject.putOpt(C.jsonKeys.CONT_IMG_URL, imageUrl);
			contestantObject.putOpt(C.jsonKeys.CONT_VOTES, voteCount);
			contestantObject.putOpt(C.jsonKeys.CONT_RANK, rank);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return contestantObject.toString();
	}
	
}
