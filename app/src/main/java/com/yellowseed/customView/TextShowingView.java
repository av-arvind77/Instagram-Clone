package com.yellowseed.customView;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yellowseed.R;


public class TextShowingView implements OnClickListener {
	private String url;
	private Activity activity;

	public TextShowingView(Activity activity, String url) {
		this.activity = activity;
		this.url = url;
	}

	public View getView() {
        View child = activity.getLayoutInflater().inflate(R.layout.view_text, null, false);
        TextView view=child.getRootView().findViewById(R.id.tvText);
        view.setText(url);
        return child;
	}

	@Override
	public void onClick(View view) {

	}


}