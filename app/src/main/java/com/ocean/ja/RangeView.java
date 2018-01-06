package com.ocean.ja;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by ocean on 2018/1/6.
 */

public class RangeView extends View {

    private long timeDiff = 0;
    private long startMS;
    private float r;
    private int xPos = 0;

    private boolean isEnlarge = false;
    private Paint paint, paintCenter;
    private int beginX;
    private float speed = 1;

    public RangeView(Context context) {
        super(context);
        init();
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);

        paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenter.setColor(Color.GREEN);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnlarge) {
            timeDiff = Calendar.getInstance().getTimeInMillis() - startMS;
            r = timeDiff * speed;
            canvas.drawCircle(xPos, getHeight() - 8, r, paint);
            postInvalidate();
        }
        canvas.drawCircle(xPos, getHeight() - 8, 8, paintCenter);
    }


    public void stop() {
        isEnlarge = false;
    }

    public void start() {
        startMS = Calendar.getInstance().getTimeInMillis();
        isEnlarge = true;
        postInvalidate();
    }

    public void moveX(int dx) {
        xPos = beginX + dx;
        if (xPos < 0) {
            xPos = 0;
        }
        if (xPos > getWidth()) {
            xPos = getWidth();
        }
        postInvalidate();
    }

    public void beginMove() {
        beginX = xPos;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
