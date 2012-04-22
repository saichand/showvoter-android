package com.achie.tv.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.achie.tv.R;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.TvShow;

import android.content.Context;
import android.graphics.Bitmap;
//import android.util.Log;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class TvShowsAdapter extends ArrayAdapter<TvShow> {

	private ViewHolder holder;
	
	private List<TvShow> mTvShows;

	private LayoutInflater mInflater;
	
	private static final String tag = "############### TvShowsAdapter :: ";
	
	public TvShowsAdapter(Context context, int resource, int textViewResourceId, ArrayList<TvShow> objects) {
		super(context, resource, textViewResourceId, objects);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mTvShows = objects;
	}
	
    public int getCount() {
        return mTvShows.size();
    }

    public TvShow getItem(int position) {
        return mTvShows.get(position);
    }
    
    /**
     * Returns the position of the specified item in the array.
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(TvShow item) {
        return mTvShows.indexOf(item);
    }

	public long getItemId(int position){
		return mTvShows.get(position).inAppId;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		Log.v(tag, "getView position :" + position);
		
		final TvShow tvShow = (TvShow) getItem(position);
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.list_item_image_two_rows, parent, false);
			holder = new ViewHolder();
			holder.tvTitle = (TextView)convertView.findViewById(R.id.tv_title);
			holder.tvDescription = (TextView)convertView.findViewById(R.id.tv_description);
			holder.ivIcon = (ImageView)convertView.findViewById(R.id.iv_icon);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(tvShow != null){
			holder.tvDescription.setText(tvShow.description);
			holder.tvTitle.setText(tvShow.showName);
			
			if (tvShow.imageUrl != null) {
				downloadImage(tvShow.imageUrl, holder.ivIcon);
			}
			
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView tvTitle;
		TextView tvDescription;
		ImageView ivIcon;
	}
	
	@Override
	public void add(TvShow tvShow) {
		super.add(tvShow);
	}
	
	
	
    private void downloadImage(String url, ImageView iv) {
    	ImageDownloader downloader = new ImageDownloader();
    	downloader.downloadImage(url, iv);
    }
	
	private class ImageDownloader {
		public void downloadImage(String url, ImageView imageView) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			task.execute(url);
		}
	}
    
	private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

		private final WeakReference<ImageView> imageViewReference;
		
		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			return TvHttpHandler.downloadBitmap(params[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}
	
}