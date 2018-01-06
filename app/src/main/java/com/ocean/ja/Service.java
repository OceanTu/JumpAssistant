package com.ocean.ja;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ocean.jumpassistant.R;

public class Service extends android.app.Service {
    private static final String TAG = Service.class.getSimpleName();
    private WindowManager mWindowManager;
    private RelativeLayout myView;
    private RangeView rangeView;
    private Handler handle;
    private static final int MSG_STOP = 1000;


    public void onCreate() {
        super.onCreate();
        handle = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_STOP:
                        rangeView.stop();
                        break;
                    default:

                }
            }
        };

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView = (RelativeLayout) inflater.inflate(R.layout.my_view, null);
        rangeView = (RangeView) myView.findViewById(R.id.predict_rang_view);

        // Add layout to window manager
        mWindowManager.addView(myView, params);

        try {
            myView.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = params;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.w(TAG, "get action " + event.getAction());
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialY = paramsT.y;
                            rangeView.beginMove();
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
//                            Log.w(TAG, "on key down");
                            break;
                        case MotionEvent.ACTION_UP:
//                            Log.w(TAG, "on key up");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int dx = (int) (event.getRawX() - initialTouchX);
                            rangeView.moveX(dx);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            mWindowManager.updateViewLayout(v, paramsT);
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            handle.removeMessages(MSG_STOP);
                            rangeView.start();
                            handle.sendEmptyMessageDelayed(MSG_STOP, 3000);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        LocalBroadcastManager.getInstance(Service.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                float speed = intent.getFloatExtra("speed", 1);
                rangeView.setSpeed(speed);

            }
        }, new IntentFilter("CHANGE_SPEED"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
