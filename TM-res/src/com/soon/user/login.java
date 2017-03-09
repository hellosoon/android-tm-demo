package com.soon.user;

import java.util.ArrayList;
import java.util.HashMap;

import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.basetools.SharedPreferencetool;
import com.soon.tm.MainActivity;
import com.soon.tm.MyApplication;
import com.soon.tm.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "NewApi", "HandlerLeak" })
public class login extends Activity {
	Button loginButton;
	EditText name, psw;
	SharedPreferencetool tool = new SharedPreferencetool(this);
	TextView textViewPassword, textViewRegister;
	protected ProgressDialog dialog;
	String accString;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				dialog.dismiss();
				Toast.makeText(login.this, "登录成功！", Toast.LENGTH_LONG).show();
				startActivity(new Intent(login.this, MainActivity.class));
				finish();
			} else {
				dialog.dismiss();
				Toast.makeText(login.this, "帐号或密码错误！", Toast.LENGTH_LONG)
						.show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		init();
	}

	private void init() {
		name = (EditText) findViewById(R.id.editTextname);
		psw = (EditText) findViewById(R.id.editTextpassword);
		textViewPassword = (TextView) findViewById(R.id.textViewpassword);
		textViewPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(login.this, "找回密码功能，暂不可用！", Toast.LENGTH_LONG)
						.show();
			}
		});
		textViewRegister = (TextView) findViewById(R.id.textViewregister);
		textViewRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(login.this, zhuce.class));
			}
		});
		loginButton = (Button) findViewById(R.id.buttonlogin);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String names = name.getText().toString().trim();
				accString = names;
				final String psws = psw.getText().toString().trim();
				if (names.isEmpty() || psws.isEmpty()) {
					Toast.makeText(login.this, "请填写您的账号密码信息！",
							Toast.LENGTH_LONG).show();
				} else {
					dialog = ProgressDialog.show(login.this, "", "正在登录，请稍后...",
							false, true, new OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {

								}
							});
					new Thread(new Runnable() {
						@Override
						public void run() {
							SendPost sendPost = new SendPost();
							String data = "";
							data = sendPost.sendPostParams(Config.LOGIN_URL,
									"acc=" + names + "&psw=" + psws);
							GetUserInfo_ArrayList getUserInfo_ArrayList = new GetUserInfo_ArrayList();
							ArrayList<HashMap<String, String>> list = getUserInfo_ArrayList
									.GetArrayList(data);
							if (list == null || list.isEmpty()) {
								handler.sendEmptyMessage(Config.FAILURE);
								return;
							}
							MyApplication.setAccount(accString);
							MyApplication.setName(list.get(0).get(
									Config.USER_NAME));
							MyApplication.setAge(list.get(0).get(
									Config.USER_AGE));
							MyApplication.setSex(list.get(0).get(
									Config.USER_SEX));
							MyApplication.setAuth_token(list.get(0).get(
									Config.USER_TOKEN));
							SharedPreferencetool sharedPreferencetool = new SharedPreferencetool(
									login.this);
							sharedPreferencetool.EditShared(Config.TM_CONFIG,
									Config.USER_TOKEN,
									list.get(0).get(Config.USER_TOKEN));
							handler.sendEmptyMessage(Config.SUCCESS);
						}
					}).start();
				}
			}
		});
	}
}
