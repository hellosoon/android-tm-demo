package com.soon.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.basetools.SharedPreferencetool;
import com.soon.monitor.TMHistory;
import com.soon.tm.R;

public class EditUserInfo extends BaseActivity {
	private EditText editText;
	private Button button, button2;
	private String param, param_name, name, temp;// Ҫ�޸ĵĲ���
	protected ProgressDialog dialog;
	private EditText editOldPass, editOldPass2;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				dialog.dismiss();
				Toast.makeText(EditUserInfo.this, "�޸ĳɹ���", Toast.LENGTH_LONG)
						.show();
				mysendbroadcast();
				finish();
			} else {
				dialog.dismiss();
				Toast.makeText(EditUserInfo.this, "�����ύʧ�ܣ������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_userinfo);
		setNeedBackGesture(true);
		init();
		editText = (EditText) findViewById(R.id.xiu_gai_xin_xi);
		editText.setText(temp);
		button = (Button) findViewById(R.id.btn_top_userinfo_xiugai_baocun);
		button2 = (Button) findViewById(R.id.btn_top_userinfo_xiugai);
		button2.setText(name);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (param_name.equals("psw")) {
					uploadUserPass();
				}
				upload_userinfo();
			}

		});
		if (param_name.equals("psw")) {
			LinearLayout lin = (LinearLayout) findViewById(R.id.edit_lin);
			editOldPass = new EditText(EditUserInfo.this);
			RelativeLayout.LayoutParams reLayoutParams = new android.widget.RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			editOldPass.setLayoutParams(reLayoutParams);
			editOldPass.setHint("����д������");
			lin.addView(editOldPass, 2);
			editOldPass2 = new EditText(EditUserInfo.this);
			RelativeLayout.LayoutParams reLayoutParams2 = new android.widget.RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			editOldPass2.setLayoutParams(reLayoutParams2);
			editOldPass2.setHint("��������д������");
			lin.addView(editOldPass2, 3);
			editText.setText("");
			editText.setHint("����д������");
		}

	}

	protected void upload_userinfo() {
		if (editText.getText().toString().trim().equals("")) {
			return;
		} else {
			dialog = ProgressDialog.show(EditUserInfo.this, "", "�����޸�...",
					false, true, new OnCancelListener() {
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
							+ sharedPreferencetool.GetSharedPre(
									Config.TM_CONFIG, Config.USER_TOKEN, "")
							+ "&" + param_name + "="
							+ editText.getText().toString().trim();
					;
					String data = sendPost.sendPostParams(
							Config.UPDATE_USER_INFO, params);

					System.out.println(data);

					if (data.contains("success")) {
						handler.sendEmptyMessage(Config.SUCCESS);
					} else {
						handler.sendEmptyMessage(Config.FAILURE);
					}
				}
			}).start();
		}
	}

	private void uploadUserPass() {
		if (editText.getText().toString().trim().equals("")
				|| editOldPass.getText().toString().trim().equals("")
				|| editOldPass2.getText().toString().trim().equals("")) {
			Toast.makeText(EditUserInfo.this, "��������Ϣ", Toast.LENGTH_SHORT)
					.show();
			return;
		} else if (editOldPass.getText().equals(editOldPass2.getText())) {
			Toast.makeText(EditUserInfo.this, "�������벻һ��", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {
			dialog = ProgressDialog.show(EditUserInfo.this, "", "�����޸�...",
					false, true, new OnCancelListener() {
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
							+ sharedPreferencetool.GetSharedPre(
									Config.TM_CONFIG, Config.USER_TOKEN, "")
							+ "&pswOld=" + editText.getText().toString().trim()
							+ "&psw=" + editOldPass.getText().toString().trim();
					;
					String data = sendPost.sendPostParams(
							Config.UPDATE_USER_INFO, params);

					System.out.println(data);

					if (data.contains("success")) {
						handler.sendEmptyMessage(Config.SUCCESS);
					} else {
						handler.sendEmptyMessage(Config.FAILURE);
					}
				}
			}).start();
		}
	}

	private void init() {
		param_name = getIntent().getStringExtra("param_name");
		name = getIntent().getStringExtra("name");
		temp = getIntent().getStringExtra("temp");
	}

	/*
	 * ���͹㲥
	 */
	void mysendbroadcast() {
		Intent intent = new Intent();
		intent.setAction("up_userinfo");
		sendBroadcast(intent);
	};
}
