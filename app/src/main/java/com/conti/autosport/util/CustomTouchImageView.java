package com.conti.autosport.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CustomTouchImageView extends AppCompatImageView {

    public List<Point> points = new ArrayList<Point>();

    Paint paint = new Paint();

    public CustomTouchImageView(Context context) {
        super(context);
    }

    // Constructor for inflating via XML
    public CustomTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        for (Point point : points) {
            i++;
            paint.setColor(Color.RED);
            canvas.drawCircle(point.x, point.y, 10, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf(i), point.x - 4, point.y + 5, paint);
            // Log.d(TAG, "Painting: "+point);
        }
    }

    public Bitmap getMyBitmapView() {
        Bitmap b = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        draw(c);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Point point = new Point();
            point.x = event.getX();
            point.y = event.getY();
            points.add(point);
            invalidate();
            Log.d("", "point: " + point);
        }
        return true;
    }

    public void eraseImage() {
        points = new ArrayList<Point>();
        points.clear();
    }
}