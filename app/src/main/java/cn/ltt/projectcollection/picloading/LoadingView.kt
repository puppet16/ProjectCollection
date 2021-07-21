package cn.ltt.projectcollection.picloading;

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.Nullable
import androidx.core.animation.addListener
import cn.ltt.projectcollection.R
import cn.ltt.projectcollection.utils.log
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/26
 * desc    旋转水波纹加载
 * ============================================================
 **/
class LoadingView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //旋转圆的画笔，六个小球的
    private val mPaint = Paint()

    //水波纹扩散圆画笔
    private val mHolePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //属性动画
    private var mValueAnimator: ValueAnimator? = null

    //背景色
    private var mBackgroundColor = Color.WHITE;

    //六个小球颜色
    private var mCircleColors = mutableListOf<Int>();

    //旋转圆的中心坐标
    private var mCenterX = 0f
    private var mCenterY = 0f

    //水波纹扩散半径
    private var mDistance = 0f

    //6个小球的半径
    private var mCircleRadius = 18F;

    //旋转圆的半径
    private var mRotateRadius = 90F;

    //当前旋转圆的旋转角度
    private var mCurrentRotateAngle = 0F;

    //当前旋转圆半径
    private var mCurrentRotateRadius = mRotateRadius;

    //扩散圆的半径
    private var mCurrentHoleRadius = 0F;

    //旋转动画时长
    private var mRotateDuration = 1200;

    private var mLoadingState: LoadingState? = null

    init {
        with(mHolePaint) {
            style = Paint.Style.STROKE
            color = mBackgroundColor
        }
        mCircleColors =
            getContext().resources.getIntArray(R.array.picture_loading_circle_colors)
                .toMutableList()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1F / 2;
        mCenterY = h * 1F / 2;
        mDistance = (hypot(w.toDouble(), h.toDouble()) / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas);
//        mLoadingState?.drawState(canvas) ?: apply {
//            mLoadingState = RotateState().apply { drawState(canvas) }
//        }
        if (mLoadingState == null) {
            mLoadingState = RotateState()
            log("onDraw--RotateState")
        }

        log("onDraw--State${mLoadingState.toString()}")
        mLoadingState!!.drawState(canvas)
    }


    private abstract class LoadingState {
        abstract fun drawState(canvas: Canvas)
    }


    //1. 旋转
    private inner class RotateState : LoadingState() {

        init {
            ValueAnimator.ofFloat(0F, (Math.PI * 2).toFloat()).apply {
                repeatCount = 2
                interpolator = LinearInterpolator()
                duration = mRotateDuration.toLong()
                addUpdateListener {
                    mCurrentRotateAngle = it.animatedValue as Float
                    invalidate()
                }
                addListener(onEnd = {
                    mLoadingState = MerginState()
                })
                start()
            }
        }

        override fun drawState(canvas: Canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制6个小球
            drawCircles(canvas);
        }
    }

    private fun drawBackground(canvas: Canvas) {
        if (mCurrentHoleRadius > 0) {
            //绘制空心圆
            val strokeWidth = mDistance - mCurrentHoleRadius
            val radius = mDistance
            mHolePaint.strokeWidth = strokeWidth
            canvas.drawCircle(mCenterX, mCenterY, radius, mHolePaint)
        } else {
            //绘制纯色背景
            canvas.drawColor(mBackgroundColor);
        }
    }

    private fun drawCircles(canvas: Canvas) {
        val rotateAngle = (Math.PI * 2 / mCircleColors.size).toFloat()
        for (i in mCircleColors.indices) {
            //x = r *cos(a) +centerX
            //y = r * sin(a) + centerY
            val angle = i * rotateAngle * 1.0 + mCurrentRotateAngle
            val cx = (cos(angle) * mCurrentRotateRadius + mCenterX).toFloat()
            val cy = (sin(angle) * mCurrentRotateRadius + mCenterY).toFloat()
            mPaint.color = mCircleColors[i];
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);

        }
    }

    //2. 扩散聚合
    private inner class MerginState : LoadingState() {

        init {
            ValueAnimator.ofFloat(mCircleRadius, mRotateRadius).apply {
                interpolator = OvershootInterpolator(10F)
                duration = mRotateDuration.toLong()
                addUpdateListener {
                    mCurrentRotateRadius = it.animatedValue as Float
                    invalidate()
                }
                addListener (onEnd = {
                    mLoadingState = ExpandState()
                })
                reverse()
            }
        }

        override fun drawState(canvas: Canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    //3. 水波纹
    private inner class ExpandState : LoadingState() {

        init {
            ValueAnimator.ofFloat(mCircleRadius, mDistance).apply {
                interpolator = LinearInterpolator()
                duration = mRotateDuration.toLong()
                addUpdateListener {
                    mCurrentHoleRadius = it.animatedValue as Float
                    invalidate()
                }
                start()
            }

        }

        override fun drawState(canvas: Canvas) {
            drawBackground(canvas);
        }
    }
}
