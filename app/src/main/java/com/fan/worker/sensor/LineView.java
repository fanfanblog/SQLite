package com.fan.worker.sensor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by pc on 19-3-18.
 */

public class LineView extends View {

    Paint paint = new Paint();
    static float lastX = 300;
    static float lastY = 500;
    static float curX = 0;
    static float curY = 0;
    public LineView(Context context) {
        super(context);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.RED);
        canvas.drawColor(Color.WHITE);
        paint.setStrokeWidth(30f);
        canvas.translate(canvas.getWidth()/2,canvas.getHeight()/2);
        canvas.drawPoint(curX,curY,paint);
        Log.i("GyroscopeSensorListener", "height = " + canvas.getHeight() + ", width = " + canvas.getWidth());
//        canvas.drawLine(lastX,lastY,curX,curY,paint);
        lastX = curX;
        lastY = curY;
    }

    public void setXY(float x, float y){
        curX = x;
        curY = y;
        invalidate();
    }
}
