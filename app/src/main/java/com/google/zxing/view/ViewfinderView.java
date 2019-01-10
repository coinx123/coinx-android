/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.coin.exchange.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.camera.CameraManager;
import com.coin.libbase.utils.UIUtils;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;
    private int CORNER_RECT_WIDTH;  //扫描区边角的宽
    private int CORNER_RECT_HEIGHT; //扫描区边角的高
    private static final int SCANNER_LINE_MOVE_DISTANCE = 5;  //扫描线移动距离
    private static final int SCANNER_LINE_HEIGHT = 8;  //扫描线宽度

    // 内缩边距
    private int insideWidth;

    private final Paint paint;
    private Bitmap resultBitmap;
    //模糊区域颜色
    private final int maskColor;
    //扫描区域边框颜色
    private final int frameColor;
    //扫描线颜色
    private final int laserColor;
    //四角颜色
    private final int cornerColor;
    //扫描区域提示文本
    private final String labelText;
    //扫描区域提示文本颜色
    private final int labelTextColor;
    private final float labelTextSize;

    public static int scannerStart = 0;
    public static int scannerEnd = 0;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化自定义属性信息
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        laserColor = array.getColor(R.styleable.ViewfinderView_laser_color, 0x00FF00);
        cornerColor = array.getColor(R.styleable.ViewfinderView_corner_color, 0x00FF00);
        frameColor = array.getColor(R.styleable.ViewfinderView_frame_color, 0xFFFFFF);
        maskColor = array.getColor(R.styleable.ViewfinderView_mask_color, 0x60000000);
        labelTextColor = array.getColor(R.styleable.ViewfinderView_label_text_color, 0x90FFFFFF);
        labelText = array.getString(R.styleable.ViewfinderView_label_text);
        labelTextSize = array.getFloat(R.styleable.ViewfinderView_label_text_size, 36f);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint();
        paint.setAntiAlias(true);

        CORNER_RECT_WIDTH = UIUtils.dip2px(context, 3);
        insideWidth = UIUtils.dip2px(context, 4) + CORNER_RECT_WIDTH;

        CORNER_RECT_HEIGHT = UIUtils.dip2px(context, 32);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        if (scannerStart == 0 || scannerEnd == 0) {
            scannerStart = frame.top + insideWidth;
            scannerEnd = frame.bottom - insideWidth;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        // Draw the exterior (i.e. outside the framing rect) darkened
        drawExterior(canvas, frame, width, height);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {
            // Draw a two pixel solid black border inside the framing rect
            drawFrame(canvas, frame);
            // 绘制边角
            drawCorner(canvas, frame);
            //绘制提示信息
            drawTextInfo(canvas, frame);
            // Draw a red "laser scanner" line through the middle to show decoding is active
            drawLaserScanner(canvas, frame);

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            //指定重绘区域，该方法会在子线程中执行
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    //绘制文本
    private void drawTextInfo(Canvas canvas, Rect frame) {
        paint.setColor(labelTextColor);
        paint.setTextSize(labelTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(labelText, frame.left + frame.width() / 2, frame.bottom + CORNER_RECT_HEIGHT * 1.5f, paint);
    }


    //绘制边角
    private void drawCorner(Canvas canvas, Rect frame) {
        paint.setColor(cornerColor);
        //左上
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_WIDTH, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_RECT_HEIGHT, frame.top + CORNER_RECT_WIDTH, paint);
        //右上
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.top, frame.right, frame.top + CORNER_RECT_HEIGHT, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.top, frame.right, frame.top + CORNER_RECT_WIDTH, paint);
        //左下
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_WIDTH, frame.left + CORNER_RECT_HEIGHT, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - CORNER_RECT_HEIGHT, frame.left + CORNER_RECT_WIDTH, frame.bottom, paint);
        //右下
        canvas.drawRect(frame.right - CORNER_RECT_WIDTH, frame.bottom - CORNER_RECT_HEIGHT, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - CORNER_RECT_HEIGHT, frame.bottom - CORNER_RECT_WIDTH, frame.right, frame.bottom, paint);
    }

    //绘制扫描线
    private void drawLaserScanner(Canvas canvas, Rect frame) {
        paint.setColor(laserColor);
        //线性渐变
        LinearGradient linearGradient = new LinearGradient(
                frame.left, scannerStart,
                frame.left, scannerStart + SCANNER_LINE_HEIGHT,
                shadeColor(laserColor),
                laserColor,
                Shader.TileMode.MIRROR);

        RadialGradient radialGradient = new RadialGradient(
                (float) (frame.left + frame.width() / 2),
                (float) (scannerStart + SCANNER_LINE_HEIGHT / 2),
                360f,
                laserColor,
                shadeColor(laserColor),
                Shader.TileMode.MIRROR);

        SweepGradient sweepGradient = new SweepGradient(
                (float) (frame.left + frame.width() / 2),
                (float) (scannerStart + SCANNER_LINE_HEIGHT),
                shadeColor(laserColor),
                laserColor);

        ComposeShader composeShader = new ComposeShader(radialGradient, linearGradient, PorterDuff.Mode.ADD);

        paint.setShader(radialGradient);
        if (scannerStart <= scannerEnd) {
            //椭圆
            RectF rectF = new RectF(frame.left + 2 * SCANNER_LINE_HEIGHT, scannerStart, frame.right - 2 * SCANNER_LINE_HEIGHT, scannerStart + SCANNER_LINE_HEIGHT);
            canvas.drawOval(rectF, paint);
            scannerStart += SCANNER_LINE_MOVE_DISTANCE;
        } else {
            scannerStart = frame.top;
        }
        paint.setShader(null);
    }

    //处理颜色模糊
    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "20" + hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    // 绘制扫描区边框 Draw a two pixel solid black border inside the framing rect
    private void drawFrame(Canvas canvas, Rect frame) {
        paint.setColor(frameColor);
        canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
        canvas.drawRect(frame.left, frame.top + 2, frame.left + 2, frame.bottom - 1, paint);
        canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
        canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1, frame.bottom + 1, paint);
    }

    // 绘制模糊区域 Draw the exterior (i.e. outside the framing rect) darkened
    private void drawExterior(Canvas canvas, Rect frame, int width, int height) {
        paint.setColor(maskColor);
        // 画上
        canvas.drawRect(0, 0, width, frame.top + insideWidth, paint);

        // 画左
        canvas.drawRect(0, frame.top + insideWidth, frame.left + insideWidth, frame.bottom - insideWidth, paint);

        // 画右
        canvas.drawRect(frame.right - insideWidth, frame.top + insideWidth, width, frame.bottom - insideWidth, paint);

        // 画右
        canvas.drawRect(0, frame.bottom - insideWidth, width, height, paint);
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
//        possibleResultPoints.add(point);
    }

}
