package com.soon.basetools;

import com.soon.tm.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

// The plan
//*-----------------------------------------*
//                  SPACE                   *
//*-----------------------------------------*
//                  TEXT                    *
//*-----------------------------------------*
//               TEXT SPACE                 *
//*-----------------------------------------*
//                  RADIUS                  *
//*-----------------------------------------*
//                   |                      *
//                   |                      *
//                   |                      *
//        ---------(x,y)--------            *
//                   |                      *
//                   |                      *
//                   |                      *
//*-----------------------------------------*
//                  RADIUS                  *
//*-----------------------------------------*
//               TEXT SPACE                 *
//*-----------------------------------------*
//                  TEXT                    *
//*-----------------------------------------*
//                  SPACE                   *
//*-----------------------------------------*


/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class ChartView extends View {

    /**
     * x轴集合
     */
    private float mXAxis[] = new float[6];

    /**
     * 白天y轴集合
     */
    private float mYAxisDay[] = new float[6];

    /**
     * 夜间y轴集合
     */
    private float mYAxisNight[] = new float[6];

    /**
     * x,y轴集合数
     */
    private static final int LENGTH = 6;

    /**
     * 白天温度集合
     */
    private int mTempDay[] = new int[6];

    /**
     * 夜间温度集合
     */
    private int mTempNight[] = new int[6];

    /**
     * 控件高
     */
    private int mHeight;

    /**
     * 字体大小
     */
    private float mTextSize;

    /**
     * 圓半径
     */
    private float mRadius;

    /**
     * 圓半径今天
     */
    private float mRadiusToday;

    /**
     * 文字移动位置距离
     */
    private float mTextSpace;

    /**
     * 白天折线颜色
     */
    private int mColorDay;

    /**
     * 夜间折线颜色
     */
    private int mColorNight;

    /**
     * 屏幕密度
     */
    private float mDensity;

    /**
     * 控件边的空白空间
     */
    private float mSpace;

    /**
     * 线画笔
     */
    private Paint mLinePaint;

    /**
     * 点画笔
     */
    private Paint mPointPaint;

    /**
     * 字体画笔
     */
    private Paint mTextPaint;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChartView);
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        mTextSize = a.getDimensionPixelSize(R.styleable.ChartView_textSize,
                (int) (14 * densityText));
        mColorDay = a.getColor(R.styleable.ChartView_dayColor,
                getResources().getColor(R.color.colorAccent));
        mColorNight = a.getColor(R.styleable.ChartView_nightColor,
                getResources().getColor(R.color.colorPrimary));

        int textColor = a.getColor(R.styleable.ChartView_textColor, Color.WHITE);
        a.recycle();

        mDensity = getResources().getDisplayMetrics().density;
        mRadius = 3 * mDensity;
        mRadiusToday = 5 * mDensity;
        mSpace = 3 * mDensity;
        mTextSpace = 10 * mDensity;

        float stokeWidth = 2 * mDensity;
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(stokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public ChartView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeight == 0) {
            // 设置控件高度，x轴集合
            setHeightAndXAxis();
        }
        // 计算y轴集合数值
        computeYAxisValues();
        // 画白天折线图
        drawChart(canvas, mColorDay, mTempDay, mYAxisDay, 0);
        // 画夜间折线图
       // drawChart(canvas, mColorNight, mTempNight, mYAxisNight, 1);
    }

    /**
     * 计算y轴集合数值
     */
    private void computeYAxisValues() {
        // 存放白天最低温度
        int minTempDay = mTempDay[0];
        // 存放白天最高温度
        int maxTempDay = mTempDay[0];
        for (int item : mTempDay) {
            if (item < minTempDay) {
                minTempDay = item;
            }
            if (item > maxTempDay) {
                maxTempDay = item;
            }
        }

        // 存放夜间最低温度
        int minTempNight = mTempNight[0];
        // 存放夜间最高温度
        int maxTempNight = mTempNight[0];
        for (int item : mTempNight) {
            if (item < minTempNight) {
                minTempNight = item;
            }
            if (item > maxTempNight) {
                maxTempNight = item;
            }
        }

        // 白天，夜间中的最低温度
        int minTemp = minTempNight < minTempDay ? minTempNight : minTempDay;
        // 白天，夜间中的最高温度
        int maxTemp = maxTempDay > maxTempNight ? maxTempDay : maxTempNight;

        // 份数（白天，夜间综合温差）
        float parts = maxTemp - minTemp;
        // y轴一端到控件一端的距离
        float length = mSpace + mTextSize + mTextSpace + mRadius;
        // y轴高度
        float yAxisHeight = mHeight - length * 2;

        // 当温度都相同时（被除数不能为0）
        if (parts == 0) {
            for (int i = 0; i < LENGTH; i++) {
                mYAxisDay[i] = yAxisHeight / 2 + length;
                mYAxisNight[i] = yAxisHeight / 2 + length;
            }
        } else {
            float partValue = yAxisHeight / parts;
            for (int i = 0; i < LENGTH; i++) {
                mYAxisDay[i] = mHeight - partValue * (mTempDay[i] - minTemp) - length;
                mYAxisNight[i] = mHeight - partValue * (mTempNight[i] - minTemp) - length;
            }
        }
    }

    /**
     * 画折线图
     *
     * @param canvas 画布
     * @param color  画图颜色
     * @param temp   温度集合
     * @param yAxis  y轴集合
     * @param type   折线种类：0，白天；1，夜间
     */
    private void drawChart(Canvas canvas, int color, int temp[], float[] yAxis, int type) {
        mLinePaint.setColor(color);
        mPointPaint.setColor(color);

        int alpha1 = 102;
        int alpha2 = 255;
        for (int i = 0; i < LENGTH; i++) {
            // 画线
            if (i < LENGTH - 1) {
                // 昨天
                if (i == 0) {
                    mLinePaint.setAlpha(alpha1);
                    // 设置虚线效果
                    mLinePaint.setPathEffect(new DashPathEffect(new float[]{2 * mDensity, 2 * mDensity}, 0));
                    // 路径
                    Path path = new Path();
                    // 路径起点
                    path.moveTo(mXAxis[i], yAxis[i]);
                    // 路径连接到
                    path.lineTo(mXAxis[i + 1], yAxis[i + 1]);
                    canvas.drawPath(path, mLinePaint);
                } else {
                    mLinePaint.setAlpha(alpha2);
                    mLinePaint.setPathEffect(null);
                    canvas.drawLine(mXAxis[i], yAxis[i], mXAxis[i + 1], yAxis[i + 1], mLinePaint);
                }
            }

            // 画点
            if (i != 1) {
                // 昨天
                if (i == 0) {
                    mPointPaint.setAlpha(alpha1);
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, mPointPaint);
                } else {
                    mPointPaint.setAlpha(alpha2);
                    canvas.drawCircle(mXAxis[i], yAxis[i], mRadius, mPointPaint);
                }
                // 今天
            } else {
                mPointPaint.setAlpha(alpha2);
                canvas.drawCircle(mXAxis[i], yAxis[i], mRadiusToday, mPointPaint);
            }

            // 画字
            // 昨天
            if (i == 0) {
                mTextPaint.setAlpha(alpha1);
                drawText(canvas, mTextPaint, i, temp, yAxis, type);
            } else {
                mTextPaint.setAlpha(alpha2);
                drawText(canvas, mTextPaint, i, temp, yAxis, type);
            }
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas    画布
     * @param textPaint 画笔
     * @param i         索引
     * @param temp      温度集合
     * @param yAxis     y轴集合
     * @param type      折线种类：0，白天；1，夜间
     */
    private void drawText(Canvas canvas, Paint textPaint, int i, int[] temp, float[] yAxis, int type) {
        switch (type) {
            case 0:
                // 显示白天气温
                canvas.drawText(temp[i] + "°", mXAxis[i], yAxis[i] - mRadius - mTextSpace, textPaint);
                break;
            case 1:
                // 显示夜间气温
                canvas.drawText(temp[i] + "°", mXAxis[i], yAxis[i] + mTextSpace + mTextSize, textPaint);
                break;
        }
    }

    /**
     * 设置高度，x轴集合
     */
    private void setHeightAndXAxis() {
        mHeight = getHeight();
        // 控件宽
        int width = getWidth();
        // 每一份宽
        float w = width / 12;
        mXAxis[0] = w;
        mXAxis[1] = w * 3;
        mXAxis[2] = w * 5;
        mXAxis[3] = w * 7;
        mXAxis[4] = w * 9;
        mXAxis[5] = w * 11;
    }

    /**
     * 设置白天温度
     *
     * @param tempDay 温度数组集合
     */
    public void setTempDay(int[] tempDay) {
        mTempDay = tempDay;
    }

    /**
     * 设置夜间温度
     *
     * @param tempNight 温度数组集合
     */
    public void setTempNight(int[] tempNight) {
        mTempNight = tempNight;
    }
}
