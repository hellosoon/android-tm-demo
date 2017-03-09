package com.soon.basetools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

@SuppressLint("ShowToast") 
public class BaseActivity extends Activity {

	/** 手势监听 */
	GestureDetector mGestureDetector;
	/** 是否需要监听手势关闭功能 */
	private boolean mNeedBackGesture = false;
	public static int FromToptoBottom = 1;
	public static int FromBottomtoTop = 2;
	public static int FromLefttoRight = 3;
	public static int FromRighttoLeft = 4;
	private long exitTime = 3000, exitTime_temp = 0;
	public SharedPreferencetool tool = new SharedPreferencetool(this);
	private int Where = 3;
	private boolean isback = false, isbacktohome = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initGestureDetector();
	}

	public final int NETTYPE_WIFI = 1;
	public final int NETTYPE_CMWAP = 2;
	public final int NETTYPE_CMNET = 3;

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	@SuppressLint({ "DefaultLocale", "NewApi" })
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!extraInfo.isEmpty()) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	public int MeasureHeight(final View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return view.getMeasuredHeight();
	}

	private void initGestureDetector() {
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(getApplicationContext(),
					new GestureListener(this));
		}
	}

	public void SetGesture(int where) {
		Where = where;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mNeedBackGesture) {
			return mGestureDetector.onTouchEvent(ev)
					|| super.dispatchTouchEvent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	/*
	 * 设置是否进行手势监听
	 */
	public void setNeedBackGesture(boolean mNeedBackGesture) {
		this.mNeedBackGesture = mNeedBackGesture;
	}

	public void setKeyBackToHome(boolean back) {
		isbacktohome = back;
	}

	public void setKeyCloseApp(boolean back) {
		isback = back;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isback) {
				exitTime = System.currentTimeMillis();
				exit();
				return false;
			} else if (isbacktohome) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			} else {
				onBackPressed();
				finish();
			}
			// overridePendingTransition(R.anim.slide_in_left,
			// R.anim.slide_out_right);
			return true;
		}
		return false;
	}

	private void exit() {
		if (exitTime - exitTime_temp <= 2000) {
			exitTime = 3000;
			exitTime_temp = 0;
			finish();
			System.exit(0);
		} else {
			exitTime_temp = System.currentTimeMillis();
			Toast.makeText(this, "再按一次退出程序", 1000).show();
		}
	}

	public class GestureListener implements OnGestureListener {

		Activity activity;

		public GestureListener(Activity activity) {

			this.activity = activity;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			switch (Where) {
			case 1:// top---bottom
				if ((e2.getY() - e1.getY()) > 180
						&& Math.abs(e1.getX() - e2.getX()) < 40) {
					activity.onBackPressed();
					activity.finish();
					return true;
				}
				break;
			case 2:// bottom---top
				if ((e1.getY() - e2.getY()) > 180
						&& Math.abs(e1.getX() - e2.getX()) < 40) {
					activity.onBackPressed();
					activity.finish();
					return true;
				}
				break;
			case 3:// left---right
				if (Math.abs((e1.getY() - e2.getY())) < 40
						&& e2.getX() - e1.getX() > 180) {
					activity.onBackPressed();
					activity.finish();
					return true;
				}
				break;
			case 4:// right---left
				if (Math.abs((e1.getY() - e2.getY())) < 40
						&& e1.getX() - e2.getX() > 180) {
					activity.onBackPressed();
					activity.finish();
					return true;
				}
				break;
			default:
				break;
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
