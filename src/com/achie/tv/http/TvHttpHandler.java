package com.achie.tv.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.achie.tv.C;

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
			response = (HttpResponse)mHttpClient.execute(post);
			entity = response.getEntity();
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
	
	public static String postVote(long showId, long contestantId) {
		HttpPost post = new HttpPost(URL_SUBMIT_VOTE + "/addVote/" + showId + "/" + contestantId);
		return doPost(post);
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
