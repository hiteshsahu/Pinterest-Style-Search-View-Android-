/**
 * 
 */
package com.serveroverload.pinterest_search.helper;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class PreferenceHelper.
 *
 * @author Hitesh
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class PreferenceHelper {

	public static final String HISTORY_COUNTER = "SavedHistoryCount";
	public static final String SEARCH_HISTORY_KEY = "savedKey";

	// No of records to hold in preferences
	public static final int MAX_SAVED_HISTORY = 5;

	/** The app context. */
	private static Context appContext;

	/** The preference helper instance. */
	private static PreferenceHelper preferenceHelperInstance = new PreferenceHelper();

	/**
	 * Instantiates a new preference helper.
	 */
	private PreferenceHelper() {
	}

	/**
	 * Gets the prefernce helper instace.
	 *
	 * @return the prefernce helper instace
	 */
	public static PreferenceHelper getPrefernceHelperInstace(Context context) {

		appContext = context;
		return preferenceHelperInstance;
	}

	/**
	 * Sets the integer.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void setInteger(String key, int value) {

		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(key, value).apply();
	}

	/**
	 * Sets the string.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void saveSearchKey(String value) {

		int currentHistoryCounter = getInteger(HISTORY_COUNTER, 1);

		if (currentHistoryCounter == MAX_SAVED_HISTORY) {

			currentHistoryCounter = 1;

			PreferenceManager.getDefaultSharedPreferences(appContext).edit()
					.putString(SEARCH_HISTORY_KEY + currentHistoryCounter, value).apply();

			currentHistoryCounter++;

			PreferenceManager.getDefaultSharedPreferences(appContext).edit()
					.putInt(HISTORY_COUNTER, currentHistoryCounter).apply();

		} else {

			PreferenceManager.getDefaultSharedPreferences(appContext).edit()
					.putString(SEARCH_HISTORY_KEY + currentHistoryCounter, value).apply();

			currentHistoryCounter++;

			PreferenceManager.getDefaultSharedPreferences(appContext).edit()
					.putInt(HISTORY_COUNTER, currentHistoryCounter).apply();
		}
	}

	/**
	 * Gets the integer.
	 *
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the integer
	 */
	public int getInteger(String key, int defaultValue) {

		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(key, defaultValue);
	}

	/**
	 * Gets the string.
	 *
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the string
	 */
	public String getString(String key, String defaultValue) {

		return PreferenceManager.getDefaultSharedPreferences(appContext).getString(key, defaultValue);
	}

	public ArrayList<String> getAllSearchHistory() {

		ArrayList<String> serachHistory = new ArrayList<String>();

		// int currentHistoryCounter = getInteger(HISTORY_COUNTER, 0);

		for (int i = 0; i < MAX_SAVED_HISTORY; i++) {
			String temp = getString(SEARCH_HISTORY_KEY + i, null);

			if (null != temp) {
				serachHistory.add(temp);
			}
		}

		return serachHistory;

	}

}
