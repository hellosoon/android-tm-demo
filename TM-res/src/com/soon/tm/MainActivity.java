package com.soon.tm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.GraphicalView;

import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.basetools.SharedPreferencetool;
import com.soon.monitor.GetTMList_ArrayList;
import com.soon.monitor.Monitor;
import com.soon.monitor.TMHistory;
import com.soon.user.UserSpace;
import com.soon.user.login;
import com.soon.user.setting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class MainActivity extends BaseActivity {
	private static int time_count = 0;
	Handler handler_time = new Handler();
	Handler handler = new Handler();
	private static double tmp = 0;
	private static float t = 0;
	private static String token = "";
	Handler chartHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				LinearLayout chartLyt = (LinearLayout) findViewById(R.id.chart);
				String data = msg.obj.toString();
				GetTMList_ArrayList getTMList_ArrayList = new GetTMList_ArrayList();
				ArrayList<HashMap<String, String>> list = getTMList_ArrayList
						.GetArrayList(data);
				if (list == null || list.isEmpty()) {
					Toast.makeText(MainActivity.this, "获取失败",
							Toast.LENGTH_SHORT).show();
				}
				List<double[]> values = new ArrayList<double[]>();
				String title = "近期温度变化";
				double[] x = new double[list.size()];
				double[] y = new double[list.size()];
				for (int i = 0; i < list.size(); i++) {
					x[i] = Double.parseDouble(list.get(i).get(
							Config.TM_DATA_TIME))
							- Double.parseDouble(list.get(list.size() - 1).get(
									Config.TM_DATA_TIME));
					y[i] = Double.parseDouble(list.get(i).get(
							Config.TM_DATA_TEMPERATURE));
				}
				values.add(x);
				values.add(y);
				Monitor chartClass = new Monitor();
				GraphicalView chartView = chartClass.addView(MainActivity.this,
						title, values);
				chartLyt.removeAllViews();//先移除所有的view再添加
				chartLyt.addView(chartView, 0);
				System.out.println("add view ~");
			} else {
				Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		@SuppressWarnings("deprecation")
		int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (systemversion > 8) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		if (MyApplication.getAuth_token() == null
				|| MyApplication.getAuth_token().isEmpty()) {
			Toast.makeText(MainActivity.this, "登录失效，请重新登录！", Toast.LENGTH_LONG)
					.show();
			Intent intent = new Intent(MainActivity.this, login.class);
			startActivity(intent);
		}
		// Intent intent = new Intent(this, MonitorService.class);
		// startService(intent);
		// System.out.println("Start polling service...");
		// PollingUtils.startPollingService(this, 80, MonitorService.class,
		// MonitorService.ACTION);
		init();
		handler_time.postDelayed(runnable_time, 1000);
	}

	Runnable runnable_time = new Runnable() {
		@Override
		public void run() {
			SharedPreferencetool sharedPreferencetool = new SharedPreferencetool(
					getApplicationContext());
			time_count++;
			if (time_count > 0) {
				tmp = Math.random();
				t = (float) (tmp * 10) + 15;
				token = sharedPreferencetool.GetSharedPre(Config.TM_CONFIG,
						Config.USER_TOKEN, "");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						SendPost sendPost = new SendPost();
						String result = sendPost.sendPostParams(Config.ADD_TM,
								"token=" + token + "&temperature=" + t);
					}
				}).start();
				String time_interval = sharedPreferencetool.GetSharedPre(
						Config.TM_CONFIG, Config.TIME_INTERVAL, "15");
				System.out.println("Start polling service..." + time_count
						+ "   " + time_interval + "   " + t);
				send();//及时更新画面
				handler.postDelayed(this,
						Integer.parseInt(time_interval) * 1000);
			}

		}
	};

	private void init() {
		findViewById(R.id.home_setting).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent(MainActivity.this,
								setting.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.home_avatar).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								UserSpace.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.btntmp).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TMHistory.class);
				startActivity(intent);
			}
		});
		send();
	}

	private void send() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				SharedPreferencetool sharedPreferencetool = new SharedPreferencetool(
						getApplicationContext());
				SendPost sendPost = new SendPost();
				String data, params;
				params = "token="
						+ sharedPreferencetool.GetSharedPre(Config.TM_CONFIG,
								Config.USER_TOKEN, "");
				;
				data = sendPost.sendPostParams(Config.GET_LIST + "10", params);
				if (data.equals("error")) {
					chartHandler.sendEmptyMessage(Config.FAILURE);
				}
				msg.what = Config.SUCCESS;
				msg.obj = data;
				chartHandler.sendMessage(msg);
			}
		}).start();
	}
}
