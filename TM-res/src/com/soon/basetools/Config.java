package com.soon.basetools;

public class Config {
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;

	public static final String LOGIN_URL = "http://tm.courses.so-on.cn/index.php/user/login";
	public static final String LOGOUT_URL = "http://tm.courses.so-on.cn/index.php/user/logout";

	public static final String REGISTER_URL = "http://tm.courses.so-on.cn/index.php/user/register";
	public static final String PASSWORD_URL = "";

	public static final String UPDATE_USER_INFO = "http://tm.courses.so-on.cn/index.php/user/update";
	public static final String GET_USER_INFO = "http://tm.courses.so-on.cn/index.php/user/getUserInfo";
	
	
	public static final String GET_LIST = "http://tm.courses.so-on.cn/index.php/tm/tlist/";
	public static final String ADD_TM = "http://tm.courses.so-on.cn/index.php/tm/add";

	public static String GET_INFO_STATE = "state";

	public static String USER_BODY = "data";

	public static String USER_ID = "user_id";
	public static String USER_NAME = "user_name";
	public static String USER_AGE = "user_age";
	public static String USER_SEX = "user_sex";
	public static String USER_ROLE = "user_role";
	public static String USER_TOKEN = "info";

	public static String TM_CONFIG = "tm_config";
	public static String TIME_INTERVAL = "time_interval";

	public static String TM_COUNT = "count";
	public static String TM_DATA = "data";
	public static String TM_DATA_USRID = "user_id";
	public static String TM_DATA_TEMPERATURE = "temperature";
	public static String TM_DATA_TIME = "time";
}
