package com.achie.tv.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TvHttpHandler {

	private static final String tag = "########## TvHttpHandler : ";
	
	private static TvHttpClient mHttpClient;
	private static final String URL_GET_SHOWS = "http://showvoter.appspot.com/showList/1/2";
	private static final String URL_SUBMIT_VOTE = "http://showvoter.appspot.com/addVote/";

	public static HttpResponse getRequest(String url) {
		final HttpGet getRequest = new HttpGet(url);
		mHttpClient = TvHttpClient.getInstance();
		
		HttpResponse response = null;
		try {
			response = mHttpClient.execute(getRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static HttpResponse postRequest(String url) {
		final HttpPost postResuest = new HttpPost(url);
		mHttpClient = TvHttpClient.getInstance();
		
		HttpResponse response = null;
		try {
			response = mHttpClient.execute(postResuest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static String doPost(HttpPost post) {
		mHttpClient = TvHttpClient.getInstance();
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		String result = "";
		try {
		    ResponseHandler<String> responseHandler=new BasicResponseHandler();
			//response = (HttpResponse)mHttpClient.execute(post);
			result = mHttpClient.execute(post, responseHandler);
			System.out.println("Result from HTTP POST: " + result);
			/*entity = response.getEntity();
			if(entity != null){
				inputStream = entity.getContent();
			}
			reader = new BufferedReader(new InputStreamReader(inputStream), 8000);
			StringBuffer builder = new StringBuffer("");
			String line = null;
			while((line = reader.readLine()) != null){
				builder.append(line+"\n");
			}
			inputStream.close();
			
			result = builder.toString();*/
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	public static String postVote(long showId, long contestantId) {
	    System.out.println("SHOWID = " + showId + " CONTESTANT ID = " + contestantId);
		//HttpPost post = new HttpPost(URL_SUBMIT_VOTE);
		
		HttpClient httpclient = new DefaultHttpClient();
	    HttpParams myParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(myParams, 10000);
	    HttpConnectionParams.setSoTimeout(myParams, 10000);

	    try {
	        JSONObject obj = new JSONObject();
	        obj.put("showId", String.valueOf(showId));
	        obj.put("contestantId", String.valueOf(contestantId));
	        HttpPost httppost = new HttpPost(URL_SUBMIT_VOTE);
	        httppost.setHeader("Content-type", "application/json");

	        StringEntity se = new StringEntity(obj.toString()); 
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httppost.setEntity(se); 

	        //HttpResponse response = httpclient.execute(httppost);
	        return doPost(httppost);
	        //String temp = EntityUtils.toString(response.getEntity());
	        //return temp;

	    } catch (IOException e) {
	    }
	    catch (JSONException e) {
	        
	    }
		//return doPost(post);
	    return null;
	}
	
    public static String getShows() {
		InputStream inputStream = null;
		BufferedReader reader = null;
		String result = "";
		mHttpClient = TvHttpClient.getInstance();
		
		try {
			HttpResponse response = mHttpClient.execute(new HttpGet(URL_GET_SHOWS));
			HttpEntity entity = response.getEntity();
			if(entity != null){
				inputStream = entity.getContent();
			}
			reader = new BufferedReader(new InputStreamReader(inputStream), 8000);
			StringBuffer builder = new StringBuffer("");
			String line = null;
			while((line = reader.readLine()) != null){
				builder.append(line+"\n");
			}
			inputStream.close();
			
			result = builder.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		return result;
    }
    
	public static Bitmap downloadBitmap(String url) {
		mHttpClient = TvHttpClient.getInstance();
		
		try {
			HttpResponse response = getRequest(url);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
