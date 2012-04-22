package com.achie.tv.http;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class TvHttpClient extends DefaultHttpClient {

	private TvHttpClient() {}
	
	private TvHttpClient(ClientConnectionManager conman, HttpParams params) {
		super(conman, params);
	}

	private static TvHttpClient myHttpClient;
	
	public static TvHttpClient getInstance() {
		if (myHttpClient == null) {
			myHttpClient = new TvHttpClient();
			ClientConnectionManager mgr = myHttpClient.getConnectionManager();
		    HttpParams params = myHttpClient.getParams();
		    
		    myHttpClient = new TvHttpClient(new ThreadSafeClientConnManager(params, 
		            mgr.getSchemeRegistry()), params);
		    
		    return myHttpClient;
		} else {
			return myHttpClient;
		}
	}
}