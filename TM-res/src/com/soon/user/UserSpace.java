package com.soon.user;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.basetools.SharedPreferencetool;
import com.soon.tm.MyApplication;
import com.soon.tm.R;

@SuppressLint("InlinedApi")
public class UserSpace extends BaseActivity {
	private Button name, age, sex, password;
	private MyBroadcastReceiver mBroadcastReceiver;
	protected ProgressDialog dialog;
	Handler handlerUpdateUserInfo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						age.setText(MyApplication.getAge());
						init_onclick(age, "age", "修改年龄", MyApplication.getAge());
						sex.setText(MyApplication.getSex());
						name.setText(MyApplication.getName());
						init_onclick(name, "name", "修改姓名",
								MyApplication.getName());
						password.setText("******");
						init_onclick(password, "psw", "修改密码", "******");
					}
				});

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_space);
		int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (systemversion > 8) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setNeedBackGesture(true);
		mBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("up_userinfo");
		registerReceiver(mBroadcastReceiver, intentFilter);
		init();
	}

	private void init() {
		name = (Button) findViewById(R.id.user_name);
		age = (Button) findViewById(R.id.user_age);
		sex = (Button) findViewById(R.id.user_sex);
		sex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[] { "男", "女" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserSpace.this);
				builder.setTitle("请选择性别：");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						up(sex, items[which], "sex");
					}
				});
				builder.create().show();
			}
		});
		password = (Button) findViewById(R.id.user_password);

		age.setText(MyApplication.getAge());
		init_onclick(age, "age", "修改年龄", MyApplication.getAge());
		sex.setText(MyApplication.getSex());
		name.setText(MyApplication.getName());
		init_onclick(name, "name", "修改姓名", MyApplication.getName());
		password.setText("******");
		init_onclick(password, "psw", "修改密码", "******");
	}

	private void up(final Button buttonss, final String stringss,
			final String stringsss) {
		dialog = ProgressDialog.show(UserSpace.this, "", "正在修改...", false,
				true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

					}
				});
		new Thread(new Runnable() {
			@Override
			public void run() {
				SendPost sendPost = new SendPost();
				SharedPreferencetool sharedPreferencetool = new SharedPreferencetool(
						getApplicationContext());
				String params = "token="
						+ sharedPreferencetool.GetSharedPre(Config.TM_CONFIG,
								Config.USER_TOKEN, "") + "&" + stringsss + "="
						+ stringss;
				;
				String data = sendPost.sendPostParams(Config.UPDATE_USER_INFO,
						params);
				if (data.contains("success")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
							buttonss.setText(stringss);
						}
					});
				} else if (data.equals("error")) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "修改失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "修改失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	private void init_onclick(Button button, final String string,
			final String value, final String temp) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserSpace.this, EditUserInfo.class);
				intent.putExtra("param_name", string);
				intent.putExtra("name", value);
				intent.putExtra("temp", temp);
				startActivity(intent);
			}
		});
	}

	/*
	 * 注册广播
	 */
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			GetUserInfo();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	private void GetUserInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SendPost sendPost = new SendPost();
				String data = "";
				SharedPreferencetool sharedPreferencetool = new SharedPreferencetool(
						getApplicationContext());
				String params = "token="
						+ sharedPreferencetool.GetSharedPre(Config.TM_CONFIG,
								Config.USER_TOKEN, "");
				data = sendPost.sendPostParams(Config.GET_USER_INFO, params);
				GetUserInfo_ArrayList getUserInfo_ArrayList = new GetUserInfo_ArrayList();
				ArrayList<HashMap<String, String>> list = getUserInfo_ArrayList
						.GetArrayList(data);
				if (list == null || list.isEmpty()) {
					handlerUpdateUserInfo.sendEmptyMessage(Config.FAILURE);
					return;
				}
				MyApplication.setName(list.get(0).get(Config.USER_NAME));
				MyApplication.setAge(list.get(0).get(Config.USER_AGE));
				MyApplication.setSex(list.get(0).get(Config.USER_SEX));
				handlerUpdateUserInfo.sendEmptyMessage(Config.SUCCESS);
			}
		}).start();
	}
}
