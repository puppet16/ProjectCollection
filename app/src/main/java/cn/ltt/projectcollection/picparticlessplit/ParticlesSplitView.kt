package cn.ltt.projectcollection.picparticlessplit;

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.Nullable
import cn.ltt.projectcollection.R
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/10
 * desc    大图会卡
 * ============================================================
 **/
class ParticlesSplitView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private val mPaint = Paint()
    private lateinit var  mBitmap: Bitmap
    private var mBalls = mutableListOf<BallBean>()
    private var d:Float = 3F;//粒子直径
    private var mAnimator: ValueAnimator? = null

    init {
        val a  = context.obtainStyledAttributes(attrs,
            R.styleable.ParticlesSplitView)
        val picResId = a.getResourceId(R.styleable.ParticlesSplitView_picResId, -1)
        if (picResId > 0) {
            mBitmap = BitmapFactory.decodeResource(getResources(), picResId)
        }
        a.recycle()
        initView()
    }

    private fun initView() {
        analysisPic()
        mAnimator = ValueAnimator.ofFloat(0F, 1F).apply {
            repeatCount = -1
            duration = 2000
            interpolator = LinearInterpolator()
            addUpdateListener {
                updateBall()
                invalidate()
            }
        }
    }

    /**
     * 更新粒子位置
     */
    private fun updateBall() {
        mBalls.forEach {ballBean ->
            ballBean.x += ballBean.vX
            ballBean.y += ballBean.vY
            ballBean.vX += ballBean.aX
            ballBean.vY += ballBean.aY
        }
    }

    public fun setPicBitmap(bitmap:Bitmap) {
        mBitmap = bitmap;
        analysisPic();
    }

    /**
     * 将图片解析成一个个粒子
     */
    private fun analysisPic() {
        mBitmap?: return
        for (i in 0 until mBitmap.width step 1){
            for (j in 0 until mBitmap.height step 1){
                mBalls.add(BallBean().apply {
                    color = mBitmap.getPixel(i, j)
                    x = i * d + d / 2
                    y = j * d + d / 2
                    r = d / 2
                    //速度(-20,20)
                    vX = rangInt(-20, 20)
                    vY = rangInt(-15, 35)
                    aX = 0F
                    aY = 0.98f
                })
            }
        }
    }

    private fun rangInt(i:Int, j:Int):Float {
        val max = max(i, j)
        val min = min(i, j) - 1
        //在0到(max - min)范围内变化，取大于x的最小整数，再随机
        return (min + ceil(Math.random() * (max - min))).toFloat()
    }

    /**
     * 将粒子画到画布上
     *
     * @param canvas
     */
    override fun onDraw(canvas:Canvas) {
        super.onDraw(canvas);
        if (mBalls.isEmpty()) {
            return
        }
        canvas.translate(200F, 300F)
        mBalls.forEach {
            mPaint.color = it.color
            canvas.drawCircle(it.x, it.y, it.r, mPaint)
        }
    }

    override fun onTouchEvent(event:MotionEvent): Boolean {
        if (MotionEvent.ACTION_DOWN == event.action) {
            mAnimator?.start()
        }
        return super.onTouchEvent(event);
    }

    override fun onDetachedFromWindow() {
        mAnimator?.cancel()
        super.onDetachedFromWindow();
    }
}
