package com.serveroverload.pinterest_search.customview;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pinterestsearchbar.R;

public class SerachKeyLayout extends RelativeLayout {

	private String searchkey;

	public String getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

//	public SerachKeyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//
//		View view = LayoutInflater.from(getContext()).inflate(
//				R.layout.search_key, null);
//		this.addView(view);
//	}
//
//	public SerachKeyLayout(Context context, AttributeSet attrs) {
//		super(context, attrs);
//
//		View view = LayoutInflater.from(getContext()).inflate(
//				R.layout.search_key, null);
//		this.addView(view);
//	}

	public SerachKeyLayout(Context context, String keyValue) {
		super(context);

		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.search_key_layout, null);

		this.searchkey = keyValue;

		((TextView) view.findViewById(R.id.key_value)).setText(keyValue);

		view.findViewById(R.id.key_holder).setBackgroundColor(
				generateRandomColor());

		this.addView(view);

	}

	final Random mRandom = new Random();;

	public int generateRandomColor() {// This is the base color which will be
										// mixed with the generated one
		final int baseColor = Color.GRAY;
		final int baseRed = Color.red(baseColor);
		final int baseGreen = Color.green(baseColor);
		final int baseBlue = Color.blue(baseColor);
		final int red = (baseRed + mRandom.nextInt(256)) / 2;
		final int green = (baseGreen + mRandom.nextInt(256)) / 2;
		final int blue = (baseBlue + mRandom.nextInt(256)) / 2;
		return Color.rgb(red, green, blue);
	}
}
// }
// }
