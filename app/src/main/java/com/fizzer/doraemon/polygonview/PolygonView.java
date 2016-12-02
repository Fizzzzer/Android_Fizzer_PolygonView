package com.fizzer.doraemon.polygonview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Fizzer on 2016/12/1.
 * Email: doraemonmqq@sina.com
 * 多边形的自定义view
 */

public class PolygonView extends View {

    /**
     * 半径，默认值为100
     */
    private int mBaseRadius = 300;

    /**
     * 多边形
     * 默认五边形
     */
    private int mPointNum = 5;

    /**
     * 内容范围的颜色值
     */
    private int contentColor = Color.parseColor("#50FF0000");

    /**
     * Base图形的颜色值
     */
    private int mBaseLineColor=Color.parseColor("#FF0000");

    /**
     * 整个View的背景颜色值
     */
    private int mBackGroundColor = Color.parseColor("#c6cacc");

    /**
     * 中心原点
     */
    Point centerPoint;


    public PolygonView(Context context) {
        super(context);
    }

    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSpec;
        } else {
            width = mBaseRadius * 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSpec;
        } else {
            height = mBaseRadius * 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //初始化中心点
        centerPoint = new Point(getWidth() / 2, getHeight() / 2);

        Paint paint = new Paint();
        paint.setColor(mBaseLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);

        canvas.drawColor(mBackGroundColor);

        drawPolygo(canvas, mBaseRadius, paint, true);
        drawPolygo(canvas, mBaseRadius - 100, paint, false);
        drawPolygo(canvas, mBaseRadius - 200, paint, false);
        drawArea(canvas);

    }

    /**
     * 绘制多边形
     *
     * @param canvas     canvas
     * @param radius     半径值
     * @param paint      paint
     * @param isDrawLine 标记是否画中心点到顶点的连接线
     */
    private void drawPolygo(Canvas canvas, int radius, Paint paint, boolean isDrawLine) {
        Path path = new Path();

        path.moveTo(centerPoint.x, centerPoint.y - radius);

        for (int index = 0; index < mPointNum; index++) {
            //获取点的坐标
            Point tmpPoint = getPoint(index, radius);
            path.lineTo(tmpPoint.x, tmpPoint.y); //将点的坐标连接起来
            if (isDrawLine) {
                canvas.drawLine(centerPoint.x, centerPoint.y, tmpPoint.x, tmpPoint.y, paint);
            }

        }
        path.close();
        canvas.drawPath(path, paint);

    }

    /**
     * 根据角度获取点的坐标
     * @param angle angle  角度
     * @return 返回目标点的坐标
     */
    private Point getPoint(float angle, int radius) {
        Point point = new Point();
        if (angle <= 90f) {
            point.x = centerPoint.x + (int) (Math.sin(angle * Math.PI / 180) * radius);
            point.y = centerPoint.y - (int) (Math.cos(angle * Math.PI / 180) * radius);
        } else if (angle <= 180f) {
            point.x = centerPoint.x + (int) (Math.cos((angle - 90f) * Math.PI / 180) * radius);
            point.y = centerPoint.y + (int) (Math.sin((angle - 90f) * Math.PI / 180) * radius);
        } else if (angle <= 270f) {
            point.x = centerPoint.x - (int) (Math.sin((angle - 180f) * Math.PI / 180) * radius);
            point.y = centerPoint.y + (int) (Math.cos((angle - 180f) * Math.PI / 180) * radius);
        } else if (angle <= 360f) {
            point.x = centerPoint.x - (int) (Math.cos((angle - 270f) * Math.PI / 180) * radius);
            point.y = centerPoint.y - (int) (Math.sin((angle - 270f) * Math.PI / 180) * radius);

        }

        return point;
    }

    /**
     * 根据索引值获取点的坐标
     * @param index 索引值
     * @param radius    半径大小
     * @return  point
     */
    private Point getPoint(int index, int radius) {
        float angle = index * 360 / mPointNum;
        return getPoint(angle, radius);

    }

    /**
     * 获取一个 0-radius 之间的整形值
     * @return  int
     */
    private int getRandom() {
        Random random = new Random();
        return random.nextInt(mBaseRadius);
    }

    /**
     * 获取所有点的做标集合
     * @return  List
     */
    private List<Point> getPointList() {
        List<Point> listPoint = new ArrayList<>();
        for (int index = 0; index < mPointNum; index++) {
            Point p = getPoint(index, getRandom());
            listPoint.add(p);
        }
        return listPoint;
    }

    /**
     * 绘制需要的范围区域
     * @param canvas    canvas
     */
    private void drawArea(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(contentColor);

        List<Point> points = getPointList();
        Path path = new Path();

        path.moveTo(points.get(0).x, points.get(0).y);
        for (int index = 1; index < points.size(); index++) {
            path.lineTo(points.get(index).x, points.get(index).y);
        }
        path.close();
        canvas.drawPath(path, paint);

    }

    /**
     * 设置整个base多边形的半径
     * @param radius 半径
     */
    public void setBaseRadius(int radius) {
        mBaseRadius = radius;
    }

    /**
     * 设置base图形的颜色
     * @param color color
     */
    public void setBaseLineColor(int color){
        mBaseLineColor = color;
    }

    /**
     * 设置view的背景颜色
     * @param color    color
     */
    public void setBackGroundColor(int color){
        mBackGroundColor = color;
    }

    /**
     * 设置顶点个数
     * @param num   num
     */
    public void setPointNum(int num){
        mPointNum = num;
    }

}
