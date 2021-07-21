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
class BezierCurveView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //计算的贝塞尔曲线的画笔
    private var mPaint = Paint()
    //path中贝塞尔曲线画笔
    private var mCodeBezierPaint = Paint()
    //文本画笔
    private var mTextPaint = Paint()
    //控制点集，未区分数据点还是控制点
    private var mControlPoints = mutableListOf<PointF> ()
    // 计算的贝塞尔曲线
    private var mPath = Path()
    //要计算曲线的轨迹点画笔
    private var mLinePaint = Paint()

    init {
        with(mPaint) {
            color = getContext().resources.getColor(R.color.green)
            style = Paint.Style.STROKE
            strokeWidth = 2F
        }

        with(mCodeBezierPaint) {
            color = getContext().resources.getColor(R.color.orange);
            style = Paint.Style.STROKE;
            strokeWidth = 2F;
        }

        with(mTextPaint) {
            color = getContext().resources.getColor(R.color.blue);
            style = Paint.Style.STROKE;
            strokeWidth = 1F;
            textSize = 20F;
        }

        with(mLinePaint) {
            color = getContext().resources.getColor(R.color.yellow);
            style = Paint.Style.STROKE;
            strokeWidth = 2F
        }

        initControlPoints();
    }

    private fun initControlPoints() {
        mControlPoints.clear()
        val random = Random()
        for (i in 0 until 9) {
            val x = random.nextInt(800) + 200*1F
            val y = random.nextInt(800) + 900*1F
            val pointF = PointF(x, y);
            mControlPoints.add(pointF);
        }
    }

    override fun onDraw(canvas:Canvas) {
        super.onDraw(canvas);
        drawCodeBezierCurve(canvas);
        drawBezierCurve(canvas);
    }

    /**
     * 画代码封装的贝塞尔曲线
     */
    private fun drawCodeBezierCurve(canvas:Canvas) {
        val path = Path().apply {
            moveTo(100F, 100F)
            canvas.drawText("一阶", 100F, 100F, mTextPaint)
            //一阶
            lineTo(300F, 300F);
            //等同于上一行代码效果，计算相对坐标
//        path.rLineTo(200, 200);

            moveTo(300F, 500F);
            canvas.drawText("二阶", 300F, 500F, mTextPaint);
            quadTo(500F, 100F, 800F, 500F)


            moveTo(400F, 600F);
            canvas.drawText("三阶", 400F, 600F, mTextPaint);
            cubicTo(500F, 100F, 600F, 900F, 900F, 600F);
        }
        canvas.drawPath(path, mCodeBezierPaint);
    }

    /**
     * 画计算的贝塞尔曲线
     */
    private fun drawBezierCurve(canvas: Canvas) {
        var point:PointF
        for (i in mControlPoints.indices) {
            point = mControlPoints[i];
            if (i > 0) {
                //控制点连线
                canvas.drawLine(mControlPoints[i - 1].x, mControlPoints[i - 1].y, point.x, point.y, mLinePaint);
            }
            if (i == 1) {
                canvas.drawText("模拟轨迹", mControlPoints[i - 1].x, mControlPoints[i - 1].y, mTextPaint);

            }
            //标示控制点
            canvas.drawCircle(point.x, point.y, 12F, mLinePaint);
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
    private fun deCasteljauX(i: Int, j:Int,t:Float):Float {
        if (i == 1) {
            return (1 - t) * mControlPoints[j].x + t * mControlPoints[j + 1].x;
        }
        return (1 - t) * deCasteljauX(i - 1, j, t) + t * deCasteljauX(i - 1, j + 1, t);
    }


    private fun deCasteljauY(i: Int, j:Int,t:Float):Float {
        if (i == 1) {
            return (1 - t) * mControlPoints[j].y + t * mControlPoints[j + 1].y;
        }
        return (1 - t) * deCasteljauY(i - 1, j, t) + t * deCasteljauY(i - 1, j + 1, t);
    }

    private fun buildBezierPoints():ArrayList<PointF> {
        mPath.reset();
        val points = arrayListOf<PointF>()
        val order = mControlPoints.size - 1;//阶数
        for (t in 0 .. 1000) {
            //画的密集度，帧
            val delta = t / 1000F
            //Bezier点集
            val pointF = PointF(deCasteljauX(order, 0, delta), deCasteljauY(order, 0, delta));
            points.add(pointF);
            if (points.size == 1) {
                mPath.moveTo(points[0].x, points[0].y);
            } else {
                mPath.lineTo(pointF.x, pointF.y);
            }
        }
        return points;
    }

    override fun onTouchEvent( event:MotionEvent):Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            initControlPoints();
            invalidate();
        }
        return true;
    }
}
