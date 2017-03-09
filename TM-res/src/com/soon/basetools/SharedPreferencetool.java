package com.soon.basetools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencetool {
	Context context;
	public SharedPreferencetool(Context context){
		this.context=context;
	}
	public void EditShared(String where,String key,String value){
		SharedPreferences sharedPreference = context.getSharedPreferences(where, Context.MODE_PRIVATE);
		Editor editor=sharedPreference.edit();
		editor.putString(key,value);
		editor.commit();
	}
	public String GetSharedPre(String where,String key,String defaultvalue){
		SharedPreferences sharedPreference = context.getSharedPreferences(where, Context.MODE_PRIVATE);
		return sharedPreference.getString(key, defaultvalue);
	}
}
