package cn.ltt.projectcollection.beziercurve;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

import cn.ltt.projectcollection.R;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/26
 * desc    贝塞尔曲线
 * ============================================================
 **/
public class BezierCurveView extends View {

    //计算的贝塞尔曲线的画笔
    private Paint mPaint;
    //path中贝塞尔曲线画笔
    private Paint mCodeBezierPaint;
    //文本画笔
    private Paint mTextPaint;
    //控制点集，未区分数据点还是控制点
    private ArrayList<PointF> mControlPoints;
    // 计算的贝塞尔曲线
    private Path mPath;
    //要计算曲线的轨迹点画笔
    private Paint mLinePaint;

    public BezierCurveView(Context context) {
        this(context, null);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mPaint = new Paint();
        mPaint.setColor(getContext().getResources().getColor(R.color.green));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        mCodeBezierPaint = new Paint();
        mCodeBezierPaint.setColor(getContext().getResources().getColor(R.color.orange));
        mCodeBezierPaint.setStyle(Paint.Style.STROKE);
        mCodeBezierPaint.setStrokeWidth(2);

        mTextPaint = new Paint();
        mTextPaint.setColor(getContext().getResources().getColor(R.color.blue));
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(20);
        mLinePaint = new Paint();
        mLinePaint.setColor(getContext().getResources().getColor(R.color.yellow));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mPath = new Path();
        mControlPoints = new ArrayList<>();
        initControlPoints();
    }

    private void initControlPoints() {
        mControlPoints.clear();
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            int x = random.nextInt(800) + 200;
            int y = random.nextInt(800) + 900;
            PointF pointF = new PointF(x, y);
            mControlPoints.add(pointF);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCodeBezierCurve(canvas);
        drawBezierCurve(canvas);
    }

    /**
     * 画代码封装的贝塞尔曲线
     */
    private void drawCodeBezierCurve(Canvas canvas) {
        Path path = new Path();
        path.moveTo(100, 100);
        canvas.drawText("一阶", 100, 100, mTextPaint);
        //一阶
        path.lineTo(300, 300);
        //等同于上一行代码效果，计算相对坐标
//        path.rLineTo(200, 200);

        path.moveTo(300, 500);
        canvas.drawText("二阶", 300, 500, mTextPaint);
        path.quadTo(500, 100, 800, 500);


        path.moveTo(400, 600);
        canvas.drawText("三阶", 400, 600, mTextPaint);
        path.cubicTo(500, 100, 600, 900, 900, 600);
        canvas.drawPath(path, mCodeBezierPaint);
    }

    /**
     * 画计算的贝塞尔曲线
     */
    private void drawBezierCurve(Canvas canvas) {
        int size = mControlPoints.size();
        PointF point;
        for (int i = 0; i < size; i++) {
            point = mControlPoints.get(i);
            if (i > 0) {
                //控制点连线
                canvas.drawLine(mControlPoints.get(i - 1).x, mControlPoints.get(i - 1).y, point.x, point.y, mLinePaint);
            }
            if (i == 1) {
                canvas.drawText("模拟轨迹", mControlPoints.get(i - 1).x, mControlPoints.get(i - 1).y, mTextPaint);

            }
            //标示控制点
            canvas.drawCircle(point.x, point.y, 12, mLinePaint);
        }
        buildBezierPoints();
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * deCasteljau算法
     * p(i,j) = (1 - t) * p(i - 1, j) + t * p(i - 1, j + 1);
     *
     * @param i 阶数
     * @param j 第几个控制点
     * @param t 时间
     * @return
     */
    private float deCasteljauX(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).x + t * mControlPoints.get(j + 1).x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t) + t * deCasteljauX(i - 1, j + 1, t);
    }


    private float deCasteljauY(int i, int j, float t) {
        if (i == 1) {
            return (1 - t) * mControlPoints.get(j).y + t * mControlPoints.get(j + 1).y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t) + t * deCasteljauY(i - 1, j + 1, t);
    }

    private ArrayList<PointF> buildBezierPoints() {
        mPath.reset();
        ArrayList<PointF> points = new ArrayList<>();
        int order = mControlPoints.size() - 1;//阶数
        //画的密集度，帧
        float delta = 1.0f / 1000;
        for (float t = 0; t <= 1; t += delta) {
            //Bezier点集
            PointF pointF = new PointF(deCasteljauX(order, 0, t), deCasteljauY(order, 0, t));
            points.add(pointF);
            if (points.size() == 1) {
                mPath.moveTo(points.get(0).x, points.get(0).y);
            } else {
                mPath.lineTo(pointF.x, pointF.y);
            }
        }
        return points;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initControlPoints();
            invalidate();
        }
        return true;
    }
}
