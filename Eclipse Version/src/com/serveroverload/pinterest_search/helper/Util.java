package com.serveroverload.pinterest_search.helper;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util {

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
		((InputMethodManager) activity
				.getSystemService(activity.INPUT_METHOD_SERVICE))
				.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

}
