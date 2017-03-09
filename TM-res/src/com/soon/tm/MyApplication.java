package com.soon.tm;

import android.app.Application;

public class MyApplication extends Application {
	private static String account = "", name = "", age = "", sex = "",
			auth_token = null;

	private static MyApplication instance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static MyApplication getInstance() {
		if (null != instance)
			instance = new MyApplication();
		return instance;
	}

	public static String getAccount() {
		return account;
	}

	public static void setAccount(String account) {
		MyApplication.account = account;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		MyApplication.name = name;
	}

	public static String getAge() {
		return age;
	}

	public static void setAge(String age) {
		MyApplication.age = age;
	}

	public static String getSex() {
		return sex;
	}

	public static void setSex(String sex) {
		MyApplication.sex = sex;
	}

	public static String getAuth_token() {
		return auth_token;
	}

	public static void setAuth_token(String auth_token) {
		MyApplication.auth_token = auth_token;
	}

}
