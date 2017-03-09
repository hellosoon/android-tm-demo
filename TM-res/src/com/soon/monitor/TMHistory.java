package com.soon.monitor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.achartengine.GraphicalView;

import DateTimePickDialogUtil.DateTimePickDialogUtil;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soon.basetools.BaseActivity;
import com.soon.basetools.Config;
import com.soon.basetools.SendPost;
import com.soon.basetools.SharedPreferencetool;
import com.soon.tm.R;

public class TMHistory extends BaseActivity {
	/** Called when the activity is first created. */
	private TextView startDateTime;
	private TextView endDateTime;

	private String initStartDateTime = "2016年11月11日 15:55"; // 初始化开始时间
	private String initEndDateTime = "2016年11月11日 15:55"; // 初始化结束时间

	private String[] dataStrings = new String[2];
	Handler chartHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (msg.what == Config.SUCCESS) {
				String data = msg.obj.toString();
				GetTMList_ArrayList getTMList_ArrayList = new GetTMList_ArrayList();
				ArrayList<HashMap<String, String>> list = getTMList_ArrayList
						.GetArrayList(data);
				if (list == null || list.isEmpty()) {
					Toast.makeText(TMHistory.this, "获取失败", Toast.LENGTH_SHORT)
							.show();
				}
				List<double[]> values = new ArrayList<double[]>();
				String title = "自定义时间段温度变化";
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
				GraphicalView chartView = chartClass.addView(TMHistory.this,
						title, values);
				LinearLayout chartLyt = (LinearLayout) findViewById(R.id.chart);
				chartLyt.addView(chartView, 0);
			} else {
				Toast.makeText(TMHistory.this, "获取失败", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		setNeedBackGesture(true);
		@SuppressWarnings("deprecation")
		int systemversion = Integer.parseInt(android.os.Build.VERSION.SDK);
		if (systemversion > 8) {
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		// 两个输入框
		startDateTime = (TextView) findViewById(R.id.inputDate);
		endDateTime = (TextView) findViewById(R.id.inputDate2);

		startDateTime.setText(initStartDateTime);
		endDateTime.setText(initEndDateTime);

		startDateTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						TMHistory.this, initEndDateTime);
				dateTimePicKDialog.dateTimePicKDialog(startDateTime);
			}
		});

		endDateTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						TMHistory.this, initEndDateTime);
				dateTimePicKDialog.dateTimePicKDialog(endDateTime);
			}
		});
		findViewById(R.id.history_search).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (startDateTime.getText().toString().trim()
								.equals("")
								|| endDateTime.getText().toString().trim()
										.equals("")) {
							return;
						}
						dataStrings[0] = String.valueOf(getCalendarByInintData(
								startDateTime.getText().toString().trim())
								.getTimeInMillis() / 1000);
						dataStrings[1] = String.valueOf(getCalendarByInintData(
								endDateTime.getText().toString().trim())
								.getTimeInMillis() / 1000);
						if (Integer.parseInt(dataStrings[1]) > Integer
								.parseInt(dataStrings[0])) {
							send(dataStrings[0], dataStrings[1]);
						} else {
							endDateTime.setText("");
							Toast.makeText(TMHistory.this, "结束日期小于其实日期！",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	private void send(final String start, final String end) {
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
								Config.USER_TOKEN, "") + "&start_time=" + start
						+ "&end_time=" + end;
				;
				data = sendPost.sendPostParams(Config.GET_LIST + "100", params);
				if (data.equals("error")) {
					chartHandler.sendEmptyMessage(Config.FAILURE);
				}
				msg.what = Config.SUCCESS;
				msg.obj = data;
				chartHandler.sendMessage(msg);
			}
		}).start();
	}

	public Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2016年11月9日 15:55拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
		String time = spliteString(initDateTime, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}
}
