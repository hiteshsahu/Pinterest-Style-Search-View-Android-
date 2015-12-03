package com.serveroverload.pinterest_search.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.serveroverload.pinterestsearchbar.R;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_activity);

		findViewById(R.id.youtube_serch_demo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toggleControlPanel(false);

				getFragmentManager().beginTransaction()
						.add(R.id.frame_root,
								PinterestWebSearchFragment.newInstance(PinterestWebSearchFragment.YouTubeMode))
						.commit();

			}
		});

		findViewById(R.id.google_search_demo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toggleControlPanel(false);

				getFragmentManager().beginTransaction().add(R.id.frame_root,
						PinterestWebSearchFragment.newInstance(PinterestWebSearchFragment.GoogleMode)).commit();

			}
		});
//
//		findViewById(R.id.music_serch_demo).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				toggleControlPanel(false);
//
//				getFragmentManager().beginTransaction().add(R.id.frame_root, new PinterestSearchWithHistoryFragment())
//						.commit();
//
//			}
//		});

		findViewById(R.id.directional_demo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toggleControlPanel(false);

				getFragmentManager().beginTransaction()
						.add(R.id.frame_root, new PinterestDirectionaSearchFragment()).commit();

			}
		});

	}

	public void toggleControlPanel(boolean shouldShowControl) {

		if (shouldShowControl) {
			findViewById(R.id.controlls_root).setVisibility(View.VISIBLE);
			findViewById(R.id.frame_root).setVisibility(View.GONE);
		} else {
			findViewById(R.id.controlls_root).setVisibility(View.GONE);
			findViewById(R.id.frame_root).setVisibility(View.VISIBLE);
		}

	}

}
