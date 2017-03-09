package com.soon.user;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SharedPreferencetool;
import com.soon.tm.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class setting extends BaseActivity {
	private Button set_time_interval;
	private SharedPreferencetool sharedPreferencetool;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		@SuppressWarnings("deprecation")
		int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (systemversion > 8) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		setNeedBackGesture(true);
		init();
	}

	private void init() {
		set_time_interval = (Button) findViewById(R.id.set_time_interval);
		sharedPreferencetool = new SharedPreferencetool(setting.this);
		String time_interval = sharedPreferencetool.GetSharedPre(
				Config.TM_CONFIG, Config.TIME_INTERVAL, "15");
		set_time_interval.setText(time_interval + "s");

		set_time_interval.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] items = new String[] { "5", "15", "30", "60" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						setting.this);
				builder.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sharedPreferencetool.EditShared(Config.TM_CONFIG,
								Config.TIME_INTERVAL, items[which]);
						set_time_interval.setText(items[which] + "s");
					}
				});
				builder.create().show();
			}
		});
	}
}
