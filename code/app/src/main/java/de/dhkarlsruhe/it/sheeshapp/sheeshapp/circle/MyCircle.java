package de.dhkarlsruhe.it.sheeshapp.sheeshapp.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by d0272129 on 16.07.18.
 */

public class MyCircle extends View {

    private static final int START_ANGLE_POINT = 90;

    private Paint paint;
    private RectF rect;
    private float angle;


    public MyCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        final int strokeWidth = 10;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.RED);
        //size 200x200 example
        rect = new RectF(strokeWidth, strokeWidth, convertDpToPx(200 - strokeWidth), convertDpToPx(200 - strokeWidth));
        System.out.println("WIDTHX:"+rect.width());
        //Initial Angle (optional, it can be zero)
        angle = 0;
    }

    public float convertDpToPx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setColor(String color) {
        try {
            paint.setColor(Color.parseColor(color));
        } catch (Exception e){
            paint.setColor(Color.parseColor("#00ff00"));
        }
    }
}

