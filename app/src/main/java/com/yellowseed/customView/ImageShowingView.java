package com.yellowseed.customView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.HomeBottomViewAdapter;
import com.yellowseed.webservices.response.homepost.Image;

import java.io.IOException;

public class ImageShowingView implements OnClickListener {
	private String url;
	private String type;
	private Activity activity;

	public ImageShowingView(Activity activity, String url,String type) {
		this.activity = activity;
		this.url = url;
		this.type = type;
	}

	public View getView() {
        View child = activity.getLayoutInflater().inflate(R.layout.view_image, null, false);
        ImageView imageView=child.getRootView().findViewById(R.id.imageView);
        Picasso.with(activity).load(url).into(imageView);
        return child;
	}
	@Override
	public void onClick(View view) {

	}


}