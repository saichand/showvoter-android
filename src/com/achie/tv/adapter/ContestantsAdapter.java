package com.achie.tv.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.achie.tv.R;
import com.achie.tv.http.TvHttpHandler;
import com.achie.tv.model.Contestant;

import android.content.Context;
import android.graphics.Bitmap;
//import android.util.Log;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ContestantsAdapter extends ArrayAdapter<Contestant> {

	private ViewHolder holder;
	
	private List<Contestant> mContestants;

	private LayoutInflater mInflater;
	
//	private static final String tag = "############### TvShowsAdapter :: ";
	
	public ContestantsAdapter(Context context, int resource, int textViewResourceId, ArrayList<Contestant> objects) {
		super(context, resource, textViewResourceId, objects);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContestants = objects;
	}
	
    public int getCount() {
        return mContestants.size();
    }

    public Contestant getItem(int position) {
        return mContestants.get(position);
    }
    
    /**
     * Returns the position of the specified item in the array.
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(Contestant item) {
        return mContestants.indexOf(item);
    }

	public long getItemId(int position){
		return mContestants.get(position).inAppId;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		

		final Contestant contestant = (Contestant) getItem(position);
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
		
		if(contestant != null){
			String title = contestant.name;
			holder.tvTitle.setText(title);
			
			if (contestant.imageUrl != null) {
				downloadImage(contestant.imageUrl, holder.ivIcon);
			}
			
			holder.tvDescription.setText(contestant.contestantInfo);
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView tvTitle;
		TextView tvDescription;
		ImageView ivIcon;
	}
	
	@Override
	public void add(Contestant tvShow) {
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