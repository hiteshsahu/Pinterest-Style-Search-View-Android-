package com.serveroverload.pinterest_search.model;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalDataHolder {

	private GlobalDataHolder() {
		
		fillDummyData();
	}

	static GlobalDataHolder globalDataHolder;

	public static GlobalDataHolder getGlobalDataHolderInstance() {
		if (null == globalDataHolder) {
			globalDataHolder = new GlobalDataHolder();
		}

		return globalDataHolder;

	}

	private HashMap<String, ArrayList<String>> dirctionalSearchMap = new HashMap<String, ArrayList<String>>();

	public HashMap<String, ArrayList<String>> getDirctionalSearchMap() {
		return dirctionalSearchMap;
	}

	// Fill data on server side according to serch key here for instance i am
	// using IOS & Android
	private void fillDummyData() {

		ArrayList<String> androidSuggestions = new ArrayList<String>();
		androidSuggestions.add("Design");
		androidSuggestions.add("Develop");
		androidSuggestions.add("Phone");
		androidSuggestions.add("App");
		androidSuggestions.add("UI");
		androidSuggestions.add("Sample");
		androidSuggestions.add("Google");

		dirctionalSearchMap.put("Android", androidSuggestions);

		ArrayList<String> iOsSuggestions = new ArrayList<String>();
		iOsSuggestions.add("iPhone");
		iOsSuggestions.add("Steve Job");
		iOsSuggestions.add("iPad");
		iOsSuggestions.add("App");
		iOsSuggestions.add("UI");
		iOsSuggestions.add("Design");
		iOsSuggestions.add("Tab");

		dirctionalSearchMap.put("iOS", iOsSuggestions);

	}
}
