package com.serveroverload.pinterest_search.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.serveroverload.pinterest_search.customview.SerachKeyLayout;
import com.serveroverload.pinterest_search.helper.PreferenceHelper;
import com.serveroverload.pinterestsearchbar.R;

@SuppressLint("NewApi")
public class PinterestSearchWithHistoryFragment extends Fragment {

	// Required for search view
	private LinearLayout searchKeyHolder;
	private EditText searchView;
	private HorizontalScrollView keyLayoutHolder;
	private TextView clearData;
	private List<String> keyList;

	// Required for demo
	ListView serachResult;
	private ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pinterest_directional_fragment, container, false);

		// Get all views
		keyLayoutHolder = (HorizontalScrollView) rootView.findViewById(R.id.key_layout_holder);
		searchKeyHolder = (LinearLayout) rootView.findViewById(R.id.serch_key_holder);
		searchView = (EditText) rootView.findViewById(R.id.search_view);
		clearData = (TextView) rootView.findViewById(R.id.remove_all);
		serachResult = (ListView) rootView.findViewById(R.id.demo_list);

		showSearchHistory();

		// make edittext visible when user touches touch search keybox holder
		keyLayoutHolder.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (keyList.size() == 0) {
					// No Keys to display as box enable edittext for searching
					keyLayoutHolder.setVisibility(View.GONE);
					searchView.setText("");
					searchView.setVisibility(View.VISIBLE);
					searchView.setSelection(searchView.getText().length());

				}

				return false;
			}
		});

		// Clear everything on UI if user choose to clar data
		clearData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchKeyHolder.removeAllViews();
				searchView.setText("");
				keyLayoutHolder.setVisibility(View.GONE);
				searchView.setVisibility(View.VISIBLE);
				searchView.requestFocus();
				searchView.setSelection(searchView.getText().length());

				// Open Keyboard to enter new search results
				showKeyboard(getActivity());

				// Display History
				showSearchHistory();

			}
		});

		// Check when edittext is empty show suggestions from history
		searchView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString().length() == 0) {
					showSearchHistory();
				}
			}
		});

		// Do serch on web on anywhere
		rootView.findViewById(R.id.search).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!searchView.getText().toString().isEmpty()) {

					// if (currentDemoMode == GoogleMode) {
					//
					// universalWebViewFragment.searchOnGoogle(searchView.getText().toString());
					//
					// } else {
					// universalWebViewFragment.searchOnYoutube(searchView.getText().toString());
					//
					// }

					// display boxes
					toggleBoxes();

					// hide keyboard while showing serach results
					hidekeyboard(getActivity());
				}

				// Store search key in Preference
				PreferenceHelper.getPrefernceHelperInstace(getActivity())
						.saveSearchKey(searchView.getText().toString());

			}
		});

		// If Done is pressed on Keyboard show search boxes and start searching
		searchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
						|| event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

					toggleBoxes();

					// if (currentDemoMode == GoogleMode) {
					//
					// universalWebViewFragment.searchOnGoogle(searchView.getText().toString());
					//
					// } else {
					// universalWebViewFragment.searchOnYoutube(searchView.getText().toString());
					//
					// }

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
		// }

		return rootView;
	}

	private void showSearchHistory() {

		// Display results from past search history
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,
				PreferenceHelper.getPrefernceHelperInstace(getActivity()).getAllSearchHistory());

		serachResult.setAdapter(adapter);
	}

	/* This method is used to hide keyboard and make serachkeyboxes */
	private void toggleBoxes() {

		// Clear everything on UI
		searchKeyHolder.removeAllViews();

		// Hide Keyboard while displaying result
		hidekeyboard(getActivity());

		// Get all keywords
		keyList = new LinkedList<String>(Arrays.asList(searchView.getText().toString().split(" ")));

		// Remove Blanks
		keyList.remove("");

		// Make Boxes for each keyword
		for (String key : keyList) {
			final SerachKeyLayout searchKeyLayout;
			searchKeyLayout = new SerachKeyLayout(getActivity(), key);
			searchKeyLayout.setGravity(Gravity.CENTER);
			
			// Handle cross in Boxes
			searchKeyLayout.findViewById(R.id.cross).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Remove keyword from list
					keyList.remove(searchKeyLayout.getSearchkey());

					// if resultant list is empty
					if (keyList.isEmpty()) {

						// Enable search box to start new search 
						keyLayoutHolder.setVisibility(View.VISIBLE);
						searchView.setVisibility(View.GONE);

					} else {

						// Update text on edittext
						
						String newKey = "";

						for (String key : keyList) {
							newKey = newKey + " " + key;
							searchView.setText(newKey);
						}
					}

					// Remove box
					searchKeyHolder.removeView(searchKeyLayout);

				}
			});

			// Touched on KeyBox
			searchKeyLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					// Enable Edittext
					keyLayoutHolder.setVisibility(View.GONE);
					searchView.setVisibility(View.VISIBLE);
					searchView.requestFocus();
					searchView.setSelection(searchView.getText().length());
					showKeyboard(getActivity());

				}
			});

			// Add Box into horizontal Linear Layout
			searchKeyHolder.addView(searchKeyLayout);

			// Store search key in Preference
			// PreferenceHelper.getPrefernceHelperInstace(getActivity())
			// .saveSearchKey(searchView.getText().toString());

		}

		// Show Boxes
		keyLayoutHolder.setVisibility(View.VISIBLE);
		searchView.setVisibility(View.GONE);
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