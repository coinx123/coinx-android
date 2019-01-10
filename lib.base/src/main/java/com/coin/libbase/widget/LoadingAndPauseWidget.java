package com.coin.libbase.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.coin.libbase.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/9/10
 * @description 进度条、暂停开始 组件 [建议24dp]
 */
public class LoadingAndPauseWidget extends View {

    private Paint mPaint;
    private Paint mCirclePaint;

    // 是否初始化过了，初始化过了就不需要再换算
    private boolean isInit = false;

    // 宽度
    private float mWidth;
    // 中心点坐标
    private Point centerPoint;

    // 暂停线宽度
    private float pauseLineWidth;
    // 暂停线高度
    private float pauseLineHeight;
    // 暂停线间隔
    private float pauseLineSpace;

    // 进度条宽度
    private float progressLineWidth;
    // 进度条半径
    private float progressLineRadius;

    // 开始左上坐标
    private Point startLeftTopPoint;
    // 开始左下坐标
    private Point startLeftBottomPoint;
    // 开始右坐标
    private Point startRightPoint;

    // 开始和停止的颜色
    private int startAndPauseColor;
    // 圆的颜色
    private int circleColor;
    // 进度条颜色
    private int progressColor;

    //    // 感叹号上半段高
//    private float errorTopHeight;
//    // 感叹号的宽
    private float errorWidth;
    //    // 感叹号的高
//    private float errorHeight;
    // 感叹号开始点
    private Point mErrorStartPoint;
    // 感叹号终止点
    private Point mErrorEndPoint;
    // 感叹号点
    private Point mBottomPoint;

    float leftPauseX;
    float rightPauseX;
    float pauseStartY;
    float pauseEndY;

    private Path mStartPath;

    // 进度
    private int mProgress;

    private RectF mRectF;

    public LoadingAndPauseWidget(Context context) {
        this(context, null, 0);
    }

    public LoadingAndPauseWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingAndPauseWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        startAndPauseColor = ContextCompat.getColor(context, R.color.jWidgetStartAndPauseColor);
        circleColor = ContextCompat.getColor(context, R.color.jWidgetCircleColor);
        progressColor = ContextCompat.getColor(context, R.color.jWidgetProgressColor);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mStartPath = new Path();
        mProgress = 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!isInit) {
            isInit = true;

            float tempWidth = getWidth() - (getPaddingTop() + getPaddingBottom());
            float tempHeight = getHeight() - (getPaddingRight() + getPaddingLeft());

            // 取最小值
            mWidth = Math.min(tempWidth, tempHeight);
            centerPoint = new Point((getWidth() - getPaddingRight()) / 2,
                    getHeight() / 2);

            calculate();
        }
    }

    /**
     * 换算所有数据
     */
    private void calculate() {

        pauseLineHeight = mWidth * 0.42f;
        pauseLineWidth = mWidth * 0.0625f;
        pauseLineSpace = mWidth * 0.17f;

        progressLineWidth = mWidth * 0.0417f;
        progressLineRadius = (mWidth - progressLineWidth) / 2;

        leftPauseX = centerPoint.x - ((pauseLineSpace / 2) + pauseLineWidth / 2);
        rightPauseX = centerPoint.x + (pauseLineSpace / 2) + pauseLineWidth / 2;

        pauseStartY = centerPoint.y - pauseLineHeight / 2;
        pauseEndY = centerPoint.y + pauseLineHeight / 2;

        float leftPointX = centerPoint.getX() - mWidth * 0.125f;
        float rightPointX = centerPoint.getX() + mWidth * 0.2083f;
        // 开始键高度
        float startHeight = mWidth * 0.42f;

        startLeftTopPoint = new Point(leftPointX, centerPoint.y - startHeight / 2);
        startLeftBottomPoint = new Point(leftPointX, centerPoint.y + startHeight / 2);
        startRightPoint = new Point(rightPointX, centerPoint.y);

        mStartPath.moveTo(startLeftTopPoint.x, startLeftTopPoint.y);
        mStartPath.lineTo(startLeftBottomPoint.x, startLeftBottomPoint.y);
        mStartPath.lineTo(startRightPoint.x, startRightPoint.y);
        mStartPath.lineTo(startLeftTopPoint.x, startLeftTopPoint.y);
        mStartPath.close();

        errorWidth = mWidth * 0.0612f;
        mErrorStartPoint = new Point(centerPoint.x, centerPoint.y - mWidth * 0.2449f);
        mErrorEndPoint = new Point(centerPoint.x, centerPoint.y + mWidth * 0.0816f);
        mBottomPoint = new Point(centerPoint.x, centerPoint.y + mWidth * 0.1837f);

        mRectF = new RectF(centerPoint.x - mWidth / 2 + progressLineWidth / 2,
                centerPoint.y - mWidth / 2 + progressLineWidth / 2,
                centerPoint.x + mWidth / 2 - progressLineWidth / 2,
                centerPoint.y + mWidth / 2 - progressLineWidth / 2);

    }

    //    private boolean isShowStart = false;
    private int status;
    public static final int LOADING = 1;
    public static final int PAUSE = 2;
    public static final int ERROR = 3;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawCircle(canvas);

        switch (status) {
            case ERROR:
                drawError(canvas);
                break;
            case PAUSE:
                drawProgress(canvas);
                drawPause(canvas);
                break;
            case LOADING:
                drawProgress(canvas);
                drawStart(canvas);
                break;
        }
    }

    public void showPause() {
        this.setProgress(PAUSE, mProgress);
    }

    public void showStart() {
        this.setProgress(LOADING, mProgress);
    }

    public void showError() {
        this.setProgress(ERROR, mProgress);
    }

    public void setProgress(int progress) {
        this.setProgress(status, progress);
    }

    /**
     * 设置进度
     *
     * @param status   显示状态
     * @param progress 进度
     */
    public void setProgress(int status, int progress) {
        this.status = status;
        this.mProgress = progress;

        postInvalidate();
    }

    /**
     * 画暂停按钮
     */
    private void drawPause(Canvas canvas) {
        mPaint.setColor(startAndPauseColor);
        mPaint.setStrokeWidth(pauseLineWidth);

        canvas.drawLine(leftPauseX, pauseStartY, leftPauseX, pauseEndY, mPaint);
        canvas.drawLine(rightPauseX, pauseStartY, rightPauseX, pauseEndY, mPaint);
    }

    private void drawStart(Canvas canvas) {
        mPaint.setColor(startAndPauseColor);
        mPaint.setStrokeWidth(1);
        canvas.drawPath(mStartPath, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mCirclePaint.setColor(progressColor);
        mCirclePaint.setStrokeWidth(progressLineWidth);

        // 不小于0
        mProgress = Math.max(mProgress, 0);
        // 不大于100
        mProgress = Math.min(mProgress, 100);

        int angle = 360 * mProgress / 100;
        canvas.drawArc(mRectF, -90, angle, false, mCirclePaint);

    }

    private void drawCircle(Canvas canvas) {
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setStrokeWidth(progressLineWidth);

        canvas.drawCircle(centerPoint.x, centerPoint.y, progressLineRadius, mCirclePaint);
    }

    private void drawError(Canvas canvas) {
        mPaint.setColor(startAndPauseColor);
        mPaint.setStrokeWidth(errorWidth);
        canvas.drawLine(mErrorStartPoint.x, mErrorStartPoint.y,
                mErrorEndPoint.x, mErrorEndPoint.y, mPaint);
        canvas.drawCircle(mBottomPoint.x, mBottomPoint.y, errorWidth / 2, mPaint);
    }

    private static class Point {
        float x;
        float y;

        private Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        private float getX() {
            return x;
        }

        private float getY() {
            return y;
        }
    }


}
