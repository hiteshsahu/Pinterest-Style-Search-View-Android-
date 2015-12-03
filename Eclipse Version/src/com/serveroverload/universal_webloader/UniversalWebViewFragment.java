package com.serveroverload.universal_webloader;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.pinterestsearchbar.R;

//No we dont give damn about Eclair or Cupcake
@SuppressLint("NewApi")
public class UniversalWebViewFragment extends Fragment {

	private static final String GOOGLE_SERACH_URL = "https://www.google.com/search?q=";

	private static final String YOUTUBE_SERACH_URL = "https://www.youtube.com/results?search_query=";

	private WebView webView;
	private FrameLayout customViewContainer;
	private WebChromeClient.CustomViewCallback customViewCallback;
	private View mCustomView;
	private myWebChromeClient mWebChromeClient;
	private myWebViewClient mWebViewClient;

	public static UniversalWebViewFragment newInstance() {

		return new UniversalWebViewFragment();
	}

	public void searchOnGoogle(String key) {
		webView.loadUrl(GOOGLE_SERACH_URL + key);
	}

	public void searchOnYoutube(String key) {
		webView.loadUrl(YOUTUBE_SERACH_URL + key);
	}

	/**
	 * Called when the activity is first created.
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.universal_web_view,
				container, false);

		customViewContainer = (FrameLayout) rootView
				.findViewById(R.id.customViewContainer);
		webView = (WebView) rootView.findViewById(R.id.webView);

		mWebViewClient = new myWebViewClient();
		webView.setWebViewClient(mWebViewClient);

		mWebChromeClient = new myWebChromeClient();
		webView.setWebChromeClient(mWebChromeClient);
		webView.getSettings().setJavaScriptEnabled(true);

		// Important for PayUMoney
		webView.getSettings().setDomStorageEnabled(true);

		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSaveFormData(true);
		webView.loadUrl(GOOGLE_SERACH_URL);
		// webView.requestFocus();

		// Handle Back keyPress
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					return true;

				}
				// return super.onKeyDown(keyCode, event);
				return true;
			}
		});

		return rootView;
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	public void onPause() {
		super.onPause(); // To change body of overridden methods use File |
							// Settings | File Templates.
		webView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume(); // To change body of overridden methods use File |
							// Settings | File Templates.
		webView.onResume();
	}

	@Override
	public void onStop() {
		super.onStop(); // To change body of overridden methods use File |
						// Settings | File Templates.
		if (inCustomView()) {
			hideCustomView();
		}
	}

	class myWebChromeClient extends WebChromeClient {
		private View mVideoProgressView;

		@Override
		public void onShowCustomView(View view, int requestedOrientation,
				CustomViewCallback callback) {
			onShowCustomView(view, callback); // To change body of overridden
												// methods use File | Settings |
												// File Templates.
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {

			// if a view already exists then immediately terminate the new one
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mCustomView = view;
			webView.setVisibility(View.GONE);
			customViewContainer.setVisibility(View.VISIBLE);
			customViewContainer.addView(view);
			customViewCallback = callback;
		}

		@Override
		public View getVideoLoadingProgressView() {

			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				mVideoProgressView = inflater.inflate(R.layout.video_progress,
						null);
			}
			return mVideoProgressView;
		}

		@Override
		public void onHideCustomView() {
			super.onHideCustomView(); // To change body of overridden methods
										// use File | Settings | File Templates.
			if (mCustomView == null)
				return;

			webView.setVisibility(View.VISIBLE);
			customViewContainer.setVisibility(View.GONE);

			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			customViewContainer.removeView(mCustomView);
			customViewCallback.onCustomViewHidden();

			mCustomView = null;
		}
	}

	class myWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		private int webViewPreviousState;

		private final int PAGE_STARTED = 0x1;

		private final int PAGE_REDIRECTED = 0x2;

		Dialog dialog = new Dialog(getActivity());

		/*
		 * (non-Javadoc)
		 * 
		 * /* (non-Javadoc)
		 * 
		 * @see
		 * android.webkit.WebViewClient#onPageStarted(android.webkit.WebView,
		 * java.lang.String, android.graphics.Bitmap)
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			webViewPreviousState = PAGE_STARTED;

			if (dialog == null || !dialog.isShowing())
				dialog = ProgressDialog.show(getActivity(), "",
						"Loading Please Wait", true, true,
						new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								// do something
							}
						});
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			if (webViewPreviousState == PAGE_STARTED) {
				if (null != dialog)
					dialog.dismiss();
				dialog = null;
			}

		}
	}

}
