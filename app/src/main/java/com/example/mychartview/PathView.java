package com.example.mychartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView extends View {

    private Paint paint;
    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.GREEN);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        path.moveTo(440,373);
        path.lineTo(467,410);
        path.lineTo(567,410);
//        path.lineTo(600,100);
        canvas.drawPath(path,paint);
//x1=440
//2020-05-14 09:56:27.715 4995-4995/? I/PieChart: drawLines: =======y1=373
//2020-05-14 09:56:27.715 4995-4995/? I/PieChart: drawLines: =======x2=467
//2020-05-14 09:56:27.715 4995-4995/? I/PieChart: drawLines: =======y2=373
    }
}
