package com.serveroverload.pinterest_search.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.serveroverload.pinterest_search.customview.SerachKeyLayout;
import com.serveroverload.pinterest_search.customview.SuggestionKeyLayout;
import com.serveroverload.pinterest_search.helper.PreferenceHelper;
import com.serveroverload.pinterest_search.helper.Util;
import com.serveroverload.pinterest_search.model.GlobalDataHolder;
import com.serveroverload.pinterestsearchbar.R;

@SuppressLint("NewApi")
public class PinterestDirectionaSearchFragment extends Fragment {

	// Required for search view
	private LinearLayout searchKeyHolder, suggestionKeyHolder;
	private EditText searchView;
	private HorizontalScrollView keyLayoutHolder, suggestionLayoutHolder;
	private TextView clearData;
	private List<String> keyList;

	// Required for demo
	ListView searchResult;
	private ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.pinterest_history_directional_fragment, container,
				false);

		// Get all views
		getAllViews(rootView);

		// Add Listeners
		handleEventsOnView(rootView);

		// Shwo data from previous Hiostory
		showSearchHistory();

		// Handle back press
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_UP
						&& keyCode == KeyEvent.KEYCODE_BACK) {

					getActivity().getFragmentManager().popBackStack();

					((MainActivity) getActivity()).toggleControlPanel(true);

				}
				return false;
			}
		});
		// }

		return rootView;
	}

	/**
	 * @param rootView
	 */
	public void handleEventsOnView(View rootView) {
		
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

					// Remove all suggestions
					suggestionKeyHolder.removeAllViews();
					suggestionLayoutHolder.setVisibility(View.GONE);
				}
				return false;
			}
		});

		// Clear everything on UI if user choose to clear data
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
				Util.showKeyboard(getActivity());

				// clear data
				keyList.clear();

				// Display History
				showSearchHistory();

				// Remove all suggestions
				suggestionKeyHolder.removeAllViews();
				suggestionLayoutHolder.setVisibility(View.GONE);

			}
		});

		// Check when edittext is empty show suggestions from history
		searchView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString().length() == 0) {
					showSearchHistory();

					// Remove all suggestions
					suggestionKeyHolder.removeAllViews();
					suggestionLayoutHolder.setVisibility(View.GONE);
				}
			}
		});

		// Do serch on web on anywhere
		rootView.findViewById(R.id.search).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						// Remove all suggestions
						suggestionKeyHolder.removeAllViews();
						suggestionLayoutHolder.setVisibility(View.GONE);

						if (!searchView.getText().toString().isEmpty()) {

							String tempKey = searchView.getText().toString()
									.replaceAll("\\s+", "");

							if (GlobalDataHolder.getGlobalDataHolderInstance()
									.getDirctionalSearchMap()
									.containsKey(tempKey)) {

								showSuggestions(tempKey);

							}

							// display boxes
							toggleBoxes();

							// hide keyboard while showing serach results
							Util.hidekeyboard(getActivity());
						}

						// Store search key in Preference
						PreferenceHelper.getPrefernceHelperInstace(
								getActivity()).saveSearchKey(
								searchView.getText().toString());

						showSearchHistory();

					}
				});

		// If Done is pressed on Keyboard show search boxes and start searching
		searchView
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| actionId == EditorInfo.IME_ACTION_DONE
								|| event.getAction() == KeyEvent.ACTION_DOWN
								&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

							// Remove all suggestions
							suggestionKeyHolder.removeAllViews();
							suggestionLayoutHolder.setVisibility(View.GONE);

							if (!searchView.getText().toString().isEmpty()) {

								toggleBoxes();

								String tempKey = searchView.getText()
										.toString().replaceAll("\\s+", "");

								if (GlobalDataHolder
										.getGlobalDataHolderInstance()
										.getDirctionalSearchMap()
										.containsKey(tempKey)) {

									showSuggestions(tempKey);

								}

								// display boxes
								toggleBoxes();

								// hide keyboard while showing serach results
								Util.hidekeyboard(getActivity());
							}

							return true;
						}
						return false;
					}

				});
	}

	/**
	 * @param rootView
	 */
	public void getAllViews(View rootView) {
		keyLayoutHolder = (HorizontalScrollView) rootView
				.findViewById(R.id.key_layout_holder);
		suggestionLayoutHolder = (HorizontalScrollView) rootView
				.findViewById(R.id.suggestion_key_layout_holder);

		searchView = (EditText) rootView.findViewById(R.id.search_view);

		searchKeyHolder = (LinearLayout) rootView
				.findViewById(R.id.serch_key_holder);
		suggestionKeyHolder = (LinearLayout) rootView
				.findViewById(R.id.suggestion_key_holder);
		clearData = (TextView) rootView.findViewById(R.id.remove_all);

		searchResult = (ListView) rootView.findViewById(R.id.demo_list);
	}

	private void showSearchHistory() {

		// Display results from past search history
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				PreferenceHelper.getPrefernceHelperInstace(getActivity())
						.getAllSearchHistory());

		searchResult.setAdapter(adapter);

		searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				searchView.setText(PreferenceHelper
						.getPrefernceHelperInstace(getActivity())
						.getAllSearchHistory().get(position));

			}
		});
	}

	/* This method is used to hide keyboard and make serachkeyboxes */
	private void toggleBoxes() {

		// Clear everything on UI
		searchKeyHolder.removeAllViews();

		// Hide Keyboard while displaying result
		Util.hidekeyboard(getActivity());

		// Get all keywords
		keyList = new LinkedList<String>(Arrays.asList(searchView.getText()
				.toString().split(" ")));

		// Remove Blanks
		keyList.remove("");

		// Make Boxes for each keyword
		for (String key : keyList) {
			final SerachKeyLayout searchKeyLayout;
			searchKeyLayout = new SerachKeyLayout(getActivity(), key);
			searchKeyLayout.setGravity(Gravity.CENTER);

			// Handle cross in Boxes
			searchKeyLayout.findViewById(R.id.cross).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							// Remove keyword from list
							keyList.remove(searchKeyLayout.getSearchkey());

							// if resultant list is empty
							if (keyList.isEmpty()) {

								// Enable search box to start new search
								keyLayoutHolder.setVisibility(View.VISIBLE);
								searchView.setVisibility(View.GONE);
								suggestionKeyHolder.removeAllViews();
								suggestionLayoutHolder.setVisibility(View.GONE);

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
					Util.showKeyboard(getActivity());

				}
			});

			// Add Box into horizontal Linear Layout
			searchKeyHolder.addView(searchKeyLayout);

			// Store search key in Preference

		}

		// Show Boxes
		keyLayoutHolder.setVisibility(View.VISIBLE);
		searchView.setVisibility(View.GONE);
	}

	private void showSuggestions(String suggestionKey) {

		// Remove all suggestions
		suggestionKeyHolder.removeAllViews();
		suggestionLayoutHolder.setVisibility(View.VISIBLE);

		ArrayList<String> suggestions = GlobalDataHolder
				.getGlobalDataHolderInstance().getDirctionalSearchMap()
				.get(suggestionKey);

		if (null != suggestions) {

			// Make Boxes for each keyword
			for (String key : suggestions) {

				final SuggestionKeyLayout suggestionKeyLayout;
				suggestionKeyLayout = new SuggestionKeyLayout(getActivity(),
						key);
				suggestionKeyLayout.setGravity(Gravity.CENTER);

				// Handle cross in Boxes
				suggestionKeyLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						final SerachKeyLayout searchKeyLayout;
						searchKeyLayout = new SerachKeyLayout(getActivity(),
								suggestionKeyLayout.getSuggestionkey());
						
						searchKeyLayout.setGravity(Gravity.CENTER);

						// // add
						keyList.add(suggestionKeyLayout.getSuggestionkey());

						// Update text on editText
						String newKey = "";

						for (String key : keyList) {
							newKey = newKey + " " + key;
							searchView.setText(newKey);
						}

						//
						// Handle cross in Boxes
						searchKeyLayout.findViewById(R.id.cross)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										// Remove keyword from list
										keyList.remove(searchKeyLayout
												.getSearchkey());

										// if resultant list is empty
										if (keyList.isEmpty()) {

											// Enable search box to start new
											// search
											keyLayoutHolder
													.setVisibility(View.VISIBLE);
											searchView.setVisibility(View.GONE);
											suggestionKeyHolder
													.removeAllViews();
											suggestionLayoutHolder
													.setVisibility(View.GONE);

										} else {

											// Update text on edittext

											String newKey = "";

											for (String key : keyList) {
												newKey = newKey + " " + key;
												searchView.setText(newKey);
											}
										}

										// Remove box
										searchKeyHolder
												.removeView(searchKeyLayout);

									}
								});

						// Touched on KeyBox
						searchKeyLayout
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										// Enable Edittext
										keyLayoutHolder
												.setVisibility(View.GONE);
										searchView.setVisibility(View.VISIBLE);
										searchView.requestFocus();
										searchView.setSelection(searchView
												.getText().length());
										Util.showKeyboard(getActivity());

									}
								});

						// Add Box into horizontal Linear Layout
						searchKeyHolder.addView(searchKeyLayout);

					}
				});

				// Add Box into horizontal Linear Layout
				suggestionKeyHolder.addView(suggestionKeyLayout);

			}
		}
	}

}