package com.soon.tm;

import com.soon.user.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        ImageView img = (ImageView) findViewById(R.id.welcomepic);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(500);
		img.setAnimation(alphaAnimation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					startActivity(new Intent(Welcome.this,
							login.class));
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
    }
}
