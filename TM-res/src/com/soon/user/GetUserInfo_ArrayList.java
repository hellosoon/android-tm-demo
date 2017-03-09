package com.soon.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.soon.basetools.Config;

public class GetUserInfo_ArrayList {

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
			HashMap<String, String> map = new HashMap<String, String>();
			if (jsonObject.getString(Config.GET_INFO_STATE).equals("success")) {
				map.put(Config.USER_TOKEN,
						jsonObject.getString(Config.USER_TOKEN));// token

				JSONObject jsonObj = jsonObject.getJSONObject(Config.USER_BODY);// 用户信息
				map.put(Config.USER_ID, jsonObj.getString(Config.USER_ID));
				System.out.println(jsonObj.getString(Config.USER_ID));
				map.put(Config.USER_NAME, jsonObj.getString(Config.USER_NAME));
				map.put(Config.USER_SEX, jsonObj.getString(Config.USER_SEX));
				map.put(Config.USER_AGE, jsonObj.getString(Config.USER_AGE));
				map.put(Config.USER_ROLE, jsonObj.getString(Config.USER_ROLE));
				list.add(map);
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
