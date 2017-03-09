package com.soon.user;

import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.tm.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class zhuce extends BaseActivity {
	int fail_2 = 2, height = 50;
	EditText name, psw, nicheng, psw_2, youxiang, yanzhengma;
	Button login, sex;
	String psws, psws_2, youxiangs, sexs, nichengs, names;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				dialog.dismiss();
				Toast.makeText(zhuce.this, "注册成功！", Toast.LENGTH_LONG).show();
				startActivity(new Intent(zhuce.this, login.class));
				finish();
			} else if (msg.what == fail_2) {
				dialog.dismiss();
				Toast.makeText(zhuce.this, "数据提交失败，请重试！", Toast.LENGTH_LONG)
						.show();
			} else {
				dialog.dismiss();
				Toast.makeText(zhuce.this, "此用户已存在！", Toast.LENGTH_LONG).show();
			}
		};
	};
	protected ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setNeedBackGesture(true);
		SetGesture(BaseActivity.FromLefttoRight);
		setContentView(R.layout.zhuce);
		// addImage(Display.dip2px(getApplicationContext(), height).intValue());
		InitView();
	}

	private void InitView() {
		name = (EditText) findViewById(R.id.register_editTextname);
		psw = (EditText) findViewById(R.id.register_editTextpassword);
		psw_2 = (EditText) findViewById(R.id.register_editTextpassword_2);
		youxiang = (EditText) findViewById(R.id.register_editTextyouxiang);
		nicheng = (EditText) findViewById(R.id.register_editTextnicheng);
		sex = (Button) findViewById(R.id.register_buttonxingbie);
		sex.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[] { "男", "女" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						zhuce.this);
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sex.setText(items[which]);
					}
				});
				builder.create().show();
			}
		});
		login = (Button) findViewById(R.id.register_buttonlogin);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				names = name.getText().toString().trim();
				psws = psw.getText().toString().trim();
				psws_2 = psw_2.getText().toString().trim();
				youxiangs = youxiang.getText().toString().trim();
				sexs = sex.getText().toString().trim();
				nichengs = nicheng.getText().toString().trim();
				if (names.isEmpty() || psws.isEmpty() || psws_2.isEmpty()
						|| nichengs.isEmpty() || sexs.isEmpty()
						|| youxiangs.isEmpty()) {
					Toast.makeText(zhuce.this, "请完善您的注册信息！", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (psws.equals(psws_2) == false) {
					Toast.makeText(zhuce.this, "两次密码不一样，请重新输入！",
							Toast.LENGTH_LONG).show();
					psw.setText("");
					psw_2.setText("");
					return;
				}
				send();
			}
		});
	}

	private void send() {
		dialog = ProgressDialog.show(zhuce.this, "", "正在注册，请稍后...", false,
				true, new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

					}
				});
		new Thread(new Runnable() {
			@Override
			public void run() {
				SendPost sendPost = new SendPost();
				String data = "", params;
				params = "acc=" + names + "&psw=" + psws + "&sex=" + sexs
						+ "&name=" + nichengs + "&age=" + youxiangs;
				data = sendPost.sendPostParams(Config.REGISTER_URL, params);
				if (data.contains("success")) {
					handler.sendEmptyMessage(Config.SUCCESS);
				} else if (data.equals("")) {
					handler.sendEmptyMessage(fail_2);
				} else {
					handler.sendEmptyMessage(Config.FAILURE);
				}
			}
		}).start();
	}
}
