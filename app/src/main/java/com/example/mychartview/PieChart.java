package com.example.mychartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PieChart extends View {
    private final String TAG = this.getClass().getSimpleName();
    private Paint mPaint;
    private int mainColor = Color.parseColor("#5DC7FE");
    private int color1 = Color.parseColor("#E1E56E");
    private int color2 = Color.parseColor("#EEBF64");
    private int color3 = Color.parseColor("#EE7B64");
    private int color4 = Color.parseColor("#5074F9");
    private int color5 = Color.parseColor("#9BD23C");
    private int color6 = Color.parseColor("#42D8B0");
    private int color7 = Color.parseColor("#5DC7FE");
    private int color8 = Color.parseColor("#508EF9");
    private int color9 = Color.parseColor("#64EEE6");
    private int width;
    private int height;
    private int radiusOut = 100;
    private int radiusInside;
    //圆心坐标
    private int cx;
    private int cy;
    //圆弧开始角度
    private int startAngle;

    public ArrayList<DataEntity> getDataleList() {
        return dataleList;
    }

    public void setDataleList(ArrayList<DataEntity> dataleList) {
        this.dataleList = dataleList;
        invalidate();
    }

    private ArrayList<DataEntity> dataleList = new ArrayList<>();
    private ArrayList<Integer> colorList = new ArrayList<>();
    private int textColor;

    public PieChart(Context context) {
        super(context);
        init();
    }


    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
        textColor = typedArray.getColor(R.styleable.PieChart_text_color, Color.parseColor("#333333"));
        radiusOut = (int) typedArray.getDimension(R.styleable.PieChart_radius_outside, 100);
        radiusInside = (int) (radiusOut * 0.66);
        init();

    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        cx = getWidth() / 2;
        cy = getHeight() / 2;
        startAngle = 0;

    }

    private void init() {
        colorList.add(color1);
        colorList.add(color2);
        colorList.add(color3);
        colorList.add(color4);
        colorList.add(color5);
        colorList.add(color6);
        colorList.add(color7);
        colorList.add(color8);
        colorList.add(color9);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外部圆
        drawCircle(canvas, mainColor, radiusOut);

        drawRect();

        drawArc(canvas);
        //绘制内部圆
        drawCircle(canvas, Color.WHITE, radiusInside);
    }

    private void drawCircle(Canvas canvas, int white, int radiusInside) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(white);
        canvas.drawCircle(cx, cy, radiusInside, mPaint);
    }

    private void drawRect() {
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);

    }

    private void drawArc(Canvas canvas) {
        mPaint.setColor(Color.GREEN);
        RectF rectF = new RectF(cx - radiusOut, cy - radiusOut, cx + radiusOut, cy + radiusOut);
        for (int i = 0; i < dataleList.size(); i++) {

            mPaint.setColor(colorList.get(i));
            mPaint.setStyle(Paint.Style.FILL);
            //数据扫过的角度
            float sweepAngle = dataleList.get(i).percent * 360;
            float endAngle = startAngle + sweepAngle/2;
            int lineOff1 = DensityUtil.dp2px(20);

            Log.i(TAG, "drawArc: =======sweepAngle" +sweepAngle);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint);
            if (endAngle > 0 && endAngle <= 90) {//第一象限
                float angle = (float) (startAngle + sweepAngle / 2.0);
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));

                int x1 = (int) (cx + radiusOut * cos);
                int y1 = (int) (cy + radiusOut * sin);
                int x2 = (int) (cx + (radiusOut + lineOff1) * cos);
                int y2 = (int) (cy + (radiusOut + lineOff1) * sin);

                drawLines(canvas, x1, y1, x2, y2, colorList.get(i),i);
            } else if (endAngle > 90 && endAngle <= 180) {//第二象限
                float angle = (float) (startAngle + sweepAngle / 2.0 - 90);
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));
                int x1 = (int) (cx - radiusOut * sin);
                int y1 = (int) (cy + radiusOut * cos);
                int x2 = (int) (cx - (radiusOut + lineOff1) * sin);
                int y2 = (int) (cy + (radiusOut + lineOff1) * cos);

                drawLines(canvas, x1, y1, x2, y2, colorList.get(i),i);
            } else if (endAngle > 180 && endAngle <= 270) {//第三象限
                float angle = (float) (startAngle + sweepAngle / 2.0 - 180);
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));
                int x1 = (int) (cx - radiusOut * cos);
                int y1 = (int) (cy - radiusOut * sin);
                int x2 = (int) (cx - (radiusOut + lineOff1) * cos);
                int y2 = (int) (cy - (radiusOut + lineOff1) * sin);

                drawLines(canvas, x1, y1, x2, y2, colorList.get(i),i);
            } else {//右上角 第四象限
                float angle = (float) ((startAngle + (sweepAngle / 2.0) - 270));
                double sin = Math.sin(Math.toRadians(angle));
                double cos = Math.cos(Math.toRadians(angle));
                int x1 = (int) (cx + radiusOut * sin);
                int y1 = (int) (cy - radiusOut * cos);
                int x2 = (int) (cx + (radiusOut + lineOff1) * sin);
                int y2 = (int) (cy - (radiusOut + lineOff1) * cos);

                drawLines(canvas, x1, y1, x2, y2, colorList.get(i),i);
            }
            startAngle += sweepAngle;

        }
        startAngle = 0;
    }

    /**
     * 画数据标识线
     *
     * @param color
     */
    private void drawLines(Canvas canvas, int x1, int y1, int x2, int y2, int color,int i) {
        int lineOff2 = DensityUtil.dp2px(80);
        int dp2 = DensityUtil.dp2px(2);
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        if (x1 > x2) {
            lineOff2 = -lineOff2;
        }
        path.lineTo(x2 + lineOff2, y2);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(color);
        canvas.drawPath(path, mPaint);

        mPaint.setColor(textColor);
        mPaint.setStyle(Paint.Style.FILL);
        String data = dataleList.get(i).title;

        Rect rect = new Rect();
        mPaint.getTextBounds(data,0,data.length(),rect);
        int textHeight = rect.bottom - rect.top;
        mPaint.setTextSize(DensityUtil.dp2px(14));
        canvas.drawText(data, x1 + lineOff2 / 2, y2 - textHeight/2, mPaint);
    }

    public static class DataEntity{
        String title;
        float percent;

        public DataEntity() {
        }

        public DataEntity(String title, float percent) {
            this.title = title;
            this.percent = percent;
        }
    }
}
