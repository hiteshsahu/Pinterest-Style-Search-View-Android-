package com.serveroverload.pinterest_search.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serveroverload.pinterest_search.customview.SerachKeyLayout;
import com.serveroverload.pinterestsearchbar.R;
import com.serveroverload.universal_webloader.UniversalWebViewFragment;

@SuppressLint("NewApi")
public class PinterestWebSearchFragment extends Fragment {

	public static final int YouTubeMode = 0;
	public static final int GoogleMode = 1;
	public final static String DEMO_MODE = "DemoMode";
	private int currentDemoMode;

	public static PinterestWebSearchFragment newInstance(int demoMode) {

		Bundle bundle = new Bundle();
		bundle.putInt(DEMO_MODE, demoMode);
		PinterestWebSearchFragment pinterestFragment = new PinterestWebSearchFragment();
		pinterestFragment.setArguments(bundle);
		return pinterestFragment;
	}

	private LinearLayout searchKeyHolder;
	private EditText searchView;
	private HorizontalScrollView keyLayoutHolder;
	private UniversalWebViewFragment universalWebViewFragment;
	private TextView clearData;
	private List<String> keyList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pinterest_normal_fragment, container, false);

		if (null != getArguments()) {
			currentDemoMode = getArguments().getInt(DEMO_MODE);

			keyLayoutHolder = (HorizontalScrollView) rootView.findViewById(R.id.key_layout_holder);
			searchKeyHolder = (LinearLayout) rootView.findViewById(R.id.serch_key_holder);
			searchView = (EditText) rootView.findViewById(R.id.search_view);
			clearData = (TextView) rootView.findViewById(R.id.remove_all);

			universalWebViewFragment = UniversalWebViewFragment.newInstance();
			getActivity().getFragmentManager().beginTransaction().add(R.id.demo_frag_root, universalWebViewFragment)
					.commit();

			keyLayoutHolder.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (keyList.size() == 0) {
						keyLayoutHolder.setVisibility(View.GONE);

						searchView.setText("");
						searchView.setVisibility(View.VISIBLE);
						searchView.setSelection(searchView.getText().length());

					}

					return false;
				}
			});

			clearData.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					searchKeyHolder.removeAllViews();
					searchView.setText("");
					keyLayoutHolder.setVisibility(View.GONE);
					searchView.setVisibility(View.VISIBLE);
					searchView.requestFocus();
					searchView.setSelection(searchView.getText().length());
					showKeyboard(getActivity());

				}
			});

			rootView.findViewById(R.id.search).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!searchView.getText().toString().isEmpty()) {

						if (currentDemoMode == GoogleMode) {

							universalWebViewFragment.searchOnGoogle(searchView.getText().toString());

						} else {
							universalWebViewFragment.searchOnYoutube(searchView.getText().toString());

						}

						hidekeyboard(getActivity());
					}
				}
			});

			searchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
							|| event.getAction() == KeyEvent.ACTION_DOWN
									&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

						searchKeyHolder.removeAllViews();

						hidekeyboard(getActivity());

						keyList = new LinkedList<String>(Arrays.asList(searchView.getText().toString().split(" ")));

						keyList.remove("");

						for (String key : keyList) {
							final SerachKeyLayout serachKeyLayout;
							serachKeyLayout = new SerachKeyLayout(getActivity(), key);
							serachKeyLayout.setGravity(Gravity.CENTER);
							serachKeyLayout.findViewById(R.id.cross).setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {

									keyList.remove(serachKeyLayout.getSearchkey());

									if (keyList.isEmpty()) {

										keyLayoutHolder.setVisibility(View.VISIBLE);
										searchView.setVisibility(View.GONE);

									} else {

										String newKey = "";

										for (String key : keyList) {

											newKey = newKey + " " + key;

											searchView.setText(newKey);
										}
									}

									searchKeyHolder.removeView(serachKeyLayout);

								}
							});

							serachKeyLayout.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated
									// method
									// stub

									keyLayoutHolder.setVisibility(View.GONE);
									searchView.setVisibility(View.VISIBLE);
									searchView.requestFocus();
									searchView.setSelection(searchView.getText().length());
									showKeyboard(getActivity());

								}
							});

							searchKeyHolder.addView(serachKeyLayout);

						}

						keyLayoutHolder.setVisibility(View.VISIBLE);
						searchView.setVisibility(View.GONE);

						if (currentDemoMode == GoogleMode) {

							universalWebViewFragment.searchOnGoogle(searchView.getText().toString());

						} else {
							universalWebViewFragment.searchOnYoutube(searchView.getText().toString());

						}

						return true;
					}
					return false;
				}
			});

			// Handle back press

			rootView.setFocusableInTouchMode(true);
			rootView.requestFocus();
			rootView.setOnKeyListener(new View.OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {

					if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

						getActivity().getFragmentManager().popBackStack();

						((MainActivity) getActivity()).toggleControlPanel(true);

					}
					return false;
				}
			});
		}

		return rootView;
	}

	public static void hidekeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// Find the currently focused view, so we can grab the correct window
		// token from it.
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		// Find the currently focused view, so we can grab the correct window
		// token from it.
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
				.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

}