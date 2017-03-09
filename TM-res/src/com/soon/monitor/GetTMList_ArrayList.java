package com.soon.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.soon.basetools.Config;

public class GetTMList_ArrayList {

	public ArrayList<HashMap<String, String>> GetArrayList(String results) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String result;
		try {
			result = new String(Base64.decode(results, Base64.DEFAULT));
		} catch (Exception e) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getString(Config.GET_INFO_STATE).equals("success")) {
				String string = jsonObject.getString(Config.TM_COUNT);
				JSONArray BodyJsonArray = jsonObject
						.getJSONArray(Config.TM_DATA);
				for (int i = 0; i < Integer.parseInt(string); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject jsonObj = BodyJsonArray.getJSONObject(i);
					map.put(Config.TM_DATA_USRID,
							jsonObj.getString(Config.TM_DATA_USRID));
					map.put(Config.TM_DATA_TEMPERATURE,
							jsonObj.getString(Config.TM_DATA_TEMPERATURE));
					map.put(Config.TM_DATA_TIME,
							jsonObj.getString(Config.TM_DATA_TIME));
					list.add(map);
				}

				return list;
			} else {
				return null;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
