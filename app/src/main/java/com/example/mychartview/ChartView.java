package com.example.mychartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;


public class ChartView extends View {
    public static final String TAG = "ChartView";
    private Paint mPaint;
    private String[] yPoints = {"3000", "6000", "9000", "12000"};
    private List<? extends XPoint> xPoints = new ArrayList<>();
    private float xStep;
    private float yStep;

    private int ox;//横坐标O
    private int oy;//纵坐标O
    private int startX = 10;//x开始坐标
    private int startY = 10;//y开始坐标
    private float columnWidth = DensityUtil.dp2px(20);//柱形宽度 px
    private int columnTextPadding = DensityUtil.dp2px(10);//文字之间的空 px
    private int yTextPadding = DensityUtil.dp2px(10);//纵坐标值距离(ox,oy)的距离
    private int paddingBottom = DensityUtil.dp2px(80);//距离视图下方 px
    private int xTextColor;
    private int yTextColor;
    private int linColor;
    private int columnColor;
    private int pointWidth = DensityUtil.dp2px(5);//刻度宽度
    private int columnTextColor;//柱状图数据颜色
    private int linePointColor;
    private int lineTextColor;
    private int lineChartColor;
    private boolean isChartDataVisible;
    private boolean isScroll = true;// //是否正在滑动
    private VelocityTracker velocityTracker;
//    private float startScrollX;

    private int viewWidth;//视图宽度
    private int viewHeight;//视图高度

    private int screenWidth;//屏幕宽度

    private int height;
    private int width;

    private float xInit;////第一个点X的坐标
    private float minXInit;//第一个点对应的最小X坐标
    private float maxXInit;//第一个点对应的最大X坐标
    private int selectIndex = 0;
    private float columnY;
    private int selectedXTitleColor = Color.parseColor("#508EF9");

    public void setStepYNumber(float stepYNumber) {
        this.stepYNumber = stepYNumber;
    }

    private float stepYNumber = 3000;//一格Y的数量值
    private boolean isColumnGradient;
    private int columnStartColor;
    private int columnEndColor;

    private int bgcolor = Color.WHITE;

    public ChartView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FF2741"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);

    }


    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomAttrs(context, attrs);
        init(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
        init(context);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        xStep = typedArray.getDimension(R.styleable.ChartView_x_step, 45);
        yStep = typedArray.getDimension(R.styleable.ChartView_y_step, 35);

        xTextColor = typedArray.getColor(R.styleable.ChartView_x_text_color, Color.BLACK);
        yTextColor = typedArray.getColor(R.styleable.ChartView_y_text_color, Color.BLACK);
        linColor = typedArray.getColor(R.styleable.ChartView_x_line_color, Color.BLUE);
        columnWidth = typedArray.getDimension(R.styleable.ChartView_column_width, 20);
        columnColor = typedArray.getColor(R.styleable.ChartView_column_color, Color.BLUE);
        isColumnGradient = typedArray.getBoolean(R.styleable.ChartView_column_color_gradient, false);
        columnStartColor = typedArray.getColor(R.styleable.ChartView_column_start_color, Color.BLUE);
        columnEndColor = typedArray.getColor(R.styleable.ChartView_column_end_color, Color.BLUE);
        columnTextColor = typedArray.getColor(R.styleable.ChartView_column_text_color, Color.BLUE);
        startX = (int) typedArray.getDimension(R.styleable.ChartView_x_padding, DensityUtil.dp2px(10));
        startY = (int) typedArray.getDimension(R.styleable.ChartView_y_padding, DensityUtil.dp2px(10));

        linePointColor = typedArray.getColor(R.styleable.ChartView_line_point_color, Color.BLUE);
        lineChartColor = typedArray.getColor(R.styleable.ChartView_line_chart_color, Color.BLUE);

        lineTextColor = typedArray.getColor(R.styleable.ChartView_line_text_color, Color.BLUE);
        isChartDataVisible = typedArray.getBoolean(R.styleable.ChartView_is_chart_data_visible, false);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        int measuredWidth = getMeasuredWidth();
//        int measuredHeight = getMeasuredHeight();
//
//        //获取视图宽度
//
//        viewWidth = (int) (0.5f + xStep * (xPoints.size() + 1) + startX);
//        //计算y轴高度 步长 * 步数
//        int yAxisHeight = (int) (yStep * yPoints.length);
//        Log.i(TAG, "onMeasure: yStep" + yStep);
//        Log.i(TAG, "onMeasure: length" + yPoints.length);
//        Log.i(TAG, "onMeasure: yAxisHeight" + yAxisHeight);
//        Log.i(TAG, "onMeasure: measuredHeight" + measuredHeight);
//        if (getMeasuredWidth() < viewWidth) {
//            measuredWidth = viewWidth;
//        }
//        if (measuredHeight < yAxisHeight) {
//            measuredHeight = yAxisHeight;
//        }
//        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.i(TAG, "onLayout  left====" + left);
        if (changed) {
            height = getHeight();
            width = getWidth();
            ox = startX;
            oy = startY + height - paddingBottom;

            xInit = startX + xStep;
            minXInit = width - ox - xStep * (xPoints.size() - 1);
            maxXInit = xInit;
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgcolor);
        drawOXYPoint(canvas);
        drawYPoints(canvas);
//        drawXPoints(canvas);
        drawColumnsAndLines(canvas);

    }

    private void drawColumnsAndLines(Canvas canvas) {
        //??
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        drawColumns(canvas);
        drawLines(canvas);

        // 将折线超出x轴坐标的部分截取掉
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(bgcolor);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        RectF rectF = new RectF(0, 0, ox, height);
        canvas.drawRect(rectF, mPaint);
        mPaint.setXfermode(null);

        //保存图层
        canvas.restoreToCount(layerId);
    }


    private void drawOXYPoint(Canvas canvas) {
        mPaint.setColor(yTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(DensityUtil.dp2px(14));
        String o = "0";
        Rect rect = new Rect();
        mPaint.getTextBounds("0", 0, o.length(), rect);
        int bottom = rect.bottom;
        int top = rect.top;
        int left = rect.left;
        int right = rect.right;
        int oWidth = right - left;
        int oHeight = bottom - top;
        canvas.drawText(o, ox - (oWidth + columnTextPadding), oy + oHeight / 2, mPaint);
//        mPaint.setColor(linColor);
//        //横线
//        canvas.drawLine(startX, startY + height - paddingBottom, startX + width, startY + height - paddingBottom, mPaint);
//        //竖线
//        canvas.drawLine(startX, startY, startX, height + startY - paddingBottom, mPaint);

        Log.i(TAG, "drawOXYPoint: ====" + ox);
        canvas.drawText(o, startX - (oWidth + columnTextPadding), oy + oHeight / 2, mPaint);
        mPaint.setColor(linColor);
        //横线
        canvas.drawLine(startX, startY + height - paddingBottom, startX + width, startY + height - paddingBottom, mPaint);
        //竖线
        canvas.drawLine(startX, startY, startX, height + startY - paddingBottom, mPaint);
    }


    /**
     * 绘制横坐标
     *
     * @param canvas
     */
    private void drawXPoints(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < xPoints.size(); i++) {
            float xLength = i * xStep;
            float pointX = xInit + xLength;

            canvas.drawLine(pointX, oy, pointX, oy - pointWidth, mPaint);

        }
    }


    /**
     * 绘制柱状图
     *
     * @param canvas
     */
    private void drawColumns(Canvas canvas) {
        for (int i = 0; i < xPoints.size(); i++) {
            float xLength = i * xStep;
            float pointX = xInit + xLength;
            float centerX = pointX;
            float columnHeight = yStep * xPoints.get(i).getxDecimal1() / stepYNumber;

            Shader shader = new LinearGradient(centerX, oy, centerX, oy - columnHeight, columnStartColor,
                    columnEndColor, Shader.TileMode.CLAMP);

            if (isColumnGradient) {
                mPaint.setShader(shader);
            } else {
                mPaint.setColor(columnColor);
            }

            //画柱形图
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(centerX - columnWidth / 2, oy - columnHeight, centerX + columnWidth / 2, oy, 0, 0, mPaint);
            } else {
                canvas.drawRect(centerX - columnWidth / 2, oy - columnHeight, centerX + columnWidth / 2, oy, mPaint);
            }
            mPaint.setShader(null);

            if (xPoints.get(i).isDataVisible) {
                //绘制柱状图数据
                String pointNum = xPoints.get(i).getxDecimal1() + "";
                Rect rectNum = new Rect();
                mPaint.getTextBounds(pointNum, 0, pointNum.length(), rectNum);
                int numRectHeight = rectNum.bottom - rectNum.top;
                int numRectWidth = rectNum.right - rectNum.left;
                mPaint.setColor(columnTextColor);
                canvas.drawText(pointNum, centerX - numRectWidth / 2, oy - columnHeight - columnTextPadding, mPaint);
                mPaint.setColor(selectedXTitleColor);
            }

            //绘制横坐标标题
            mPaint.setColor(xTextColor);
            String title = xPoints.get(i).getTitle();
            Rect rect = new Rect();
            mPaint.getTextBounds(title, 0, title.length(), rect);
            int rectHeight = rect.bottom - rect.top;
            int rectWidth = rect.right - rect.left;
            canvas.drawText(title, centerX - rectWidth / 2, oy + rectHeight + columnTextPadding, mPaint);
        }

    }

    /**
     * 绘制纵坐标
     *
     * @param canvas
     */
    private void drawYPoints(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < yPoints.length; i++) {
            mPaint.setColor(linColor);
            float yLength = (i + 1) * yStep;
            float pointY = oy - yLength;
            canvas.drawLine(startX, pointY, startX + pointWidth, pointY, mPaint);
            mPaint.setColor(yTextColor);
            String pointStr = yPoints[i];
            Rect rect = new Rect();
            mPaint.getTextBounds(pointStr, 0, pointStr.length(), rect);
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;

            canvas.drawText(pointStr, ox - (width + yTextPadding), pointY + height / 2, mPaint);

        }

    }

    /**
     * 绘制折线图
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        if (xPoints.size() == 0) {
            return;
        }
        Point[] mPoints = new Point[xPoints.size()];
//        float[] pointsy = new float[xPoints.size()];
        for (int i = 0; i < xPoints.size(); i++) {

            float xLength = i * xStep;
            float pointX = xInit + xLength;
            //计算圆圈坐标
            float pointY = yStep * xPoints.get(i).getxDecimal2() / stepYNumber;
            mPaint.setColor(linePointColor);

            mPaint.setStyle(Paint.Style.FILL);

            //绘制圆圈
            canvas.drawCircle(pointX, oy - pointY, DensityUtil.dp2px(3), mPaint);
            //绘制圆圈上的数值
            if (xPoints.get(i).isDataVisible) {
                String pointNum = xPoints.get(i).getxDecimal2() + "";
                Rect rectNum = new Rect();
                mPaint.getTextBounds(pointNum, 0, pointNum.length(), rectNum);
                int numRectWidth = rectNum.right - rectNum.left;
                mPaint.setColor(lineTextColor);
                canvas.drawText(pointNum, pointX - numRectWidth / 2, oy - pointY - columnTextPadding, mPaint);
            }


            //绘制贝塞尔曲线
            Point point = new Point((int) (0.5f + pointX), (int) (0.5f + oy - pointY));
            mPoints[i] = point;
            if (i > 0 && i <= xPoints.size() - 1) {
                mPaint.setStyle(Paint.Style.STROKE);
                Point startp = mPoints[i - 1];
                Point endp = mPoints[i];
                int wt = (startp.x + endp.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;

                Path path = new Path();
                path.moveTo(startp.x, startp.y);
                //二次贝塞尔曲线
//                path.quadTo(p3.x,p3.y,endp.x,endp.y);
                //绘制三次贝塞尔曲线
                path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);

                canvas.drawPath(path, mPaint);
            }
        }


    }


    int lastX;
    int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (isScroll){
//            return super.onTouchEvent(event);
//        }
        this.getParent().requestDisallowInterceptTouchEvent(true);
        obtainVelocityTracker(event);


        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (xStep*xPoints.size() + startX > getWidth()){
                    int offsetx = (int) (event.getX() - lastX);
                    lastX = (int) event.getX();
                    if (xInit + offsetx < minXInit) {
                        xInit = (int) minXInit;
                    } else if (xInit + offsetx > maxXInit) {
                        xInit = (int) maxXInit;
                    } else {
                        xInit = xInit + offsetx;
                    }
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() - lastX < 5) {
                    clickAction(event);
                }
//                scrollAfterActionUp();
                this.getParent().requestDisallowInterceptTouchEvent(false);
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return true;
    }

    private void startScroll(float x) {
        int scrollX = (int) Math.ceil(x);
        layout(-scrollX, getTop(), getRight() - scrollX, getBottom());

    }

    /**
     * 回收速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 点击X轴坐标或者折线节点
     *
     * @param event
     */
    private void clickAction(MotionEvent event) {
        int dp8 = DensityUtil.dp2px(10);
        float eventX = event.getX();
        float eventY = event.getY();
        for (int i = 0; i < xPoints.size(); i++) {
            //节点
            float x = xInit + xStep * i;

            float linePointY = yStep * xPoints.get(i).getxDecimal2() / stepYNumber;
            float columnY = yStep * xPoints.get(i).getxDecimal1() / stepYNumber;
            float y1 = oy - linePointY;
            float y2 = oy - columnY;

            if (eventX >= x - dp8 && eventX <= x + dp8 &&
                    eventY >= y1 - dp8 && eventY <= y1 + dp8 && selectIndex != i + 1) {//每个节点周围8dp都是可点击区域
                if (selectIndex == i) {
                    xPoints.get(i).isDataVisible = false;
                } else {
                    xPoints.get(selectIndex) .isDataVisible = false;
                    xPoints.get(i).isDataVisible = true;
                }
                selectIndex = i;

                invalidate();
                return;
            }
            if (eventX >= x - dp8 && eventX <= x + dp8 &&
                    eventY >= y2 - dp8 && eventY <= y2 + dp8 && selectIndex != i + 1) {//每个节点周围8dp都是可点击区域
                selectIndex = i + 1;
                invalidate();
                return;
            }

        }
    }

    /**
     * 获取速度跟踪器
     *
     * @param event
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (!isScroll)
            return;
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
    }


    public void setY(String[] xList) {
        yPoints = xList;
        invalidate();
    }

    public void setX(List<? extends XPoint> xPoints) {
        this.xPoints = xPoints;
        invalidate();
    }

    public void clearData() {
        xPoints.clear();
        invalidate();
    }


    public static class XPoint {
        float xDecimal1;
        float xDecimal2;

        boolean isDataVisible;


        public float getxDecimal2() {
            return xDecimal2;
        }

        public void setxDecimal2(float xDecimal2) {
            this.xDecimal2 = xDecimal2;
        }

        public XPoint(String title, float xDecimal1, float xDecimal2) {
            this.xDecimal1 = xDecimal1;
            this.xDecimal2 = xDecimal2;
            this.title = title;
        }

        public XPoint(float xDecimal1, String title) {
            this.xDecimal1 = xDecimal1;
            this.title = title;
        }

        public XPoint() {
        }

        public float getxDecimal1() {
            return xDecimal1;
        }

        public void setxDecimal1(float xDecimal1) {
            this.xDecimal1 = xDecimal1;
        }

        public XPoint(String title) {
            this.title = title;
        }

        String title;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
