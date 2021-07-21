package cn.ltt.projectcollection.dragbubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.animation.addListener

import cn.ltt.projectcollection.R;
import kotlin.math.hypot

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/11/6
 * desc    仿qq消息气泡
 * ============================================================
 **/
class DragBubbleView @JvmOverloads constructor(
    context: Context,
    @Nullable val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //气泡默认状态-静止
    private val BUBBLE_STATE_DEFAULT = 0

    //气泡相连
    private val BUBBLE_STATE_CONNECT = 1

    //气泡分离
    private val BUBBLE_STATE_APART = 2

    //气泡消失
    private val BUBBLE_STATE_DISMISS = 3

    //气泡半径
    private var mBubbleRadius = 0F

    //气泡颜色
    private var mBubbleColor = 0

    //气泡消息文字
    private var mTextStr = ""

    //气泡消息文字大小
    private var mTextSize = 0F

    //气泡消息文字颜色
    private var mTextColor = 0

    //不动气泡的半径
    private var mBubFixedRadius = 0F

    //可动气泡的半径
    private var mBubMovableRadius = 0F

    //不动气泡的圆心
    private var mBubFixedCenter: PointF? = null

    //可动气泡圆心
    private var mBubMoveableCenter: PointF? = null

    //气泡画笔
    private lateinit var mBubblePaint: Paint;
    private lateinit var mTextPaint: Paint

    //贝塞尔曲线path
    private lateinit var mBezierPath: Path

    //文本绘制区域
    private lateinit var mTextRect: Rect
    private lateinit var mBurstPaint: Paint

    //爆炸绘制区域
    private lateinit var mBurstRect: Rect

    //气泡状态标志
    private var mBubbleState = BUBBLE_STATE_DEFAULT

    //两气泡圆心距离
    private var mDist = 0F;

    //气泡相连状态最大圆心距离
    private var mMaxDist = 0F;

    //手指触摸偏移量
    private var MOVE_OFFSET = 0F;

    //气泡爆炸的bitmap数组
    private var mBurstBitmapsArray = arrayOf<Bitmap>()

    //是否在执行气泡爆炸动画
    private var mIsBurstAnimStart = false;

    //当前气泡爆炸图片index
    private var mCurDrawableIndex = 0

    //气泡爆炸的图片数组
    private var mBurstDrawablesArray = intArrayOf(
        R.mipmap.ico_drag_bubble_burst_1,
        R.mipmap.ico_drag_bubble_burst_2,
        R.mipmap.ico_drag_bubble_burst_3,
        R.mipmap.ico_drag_bubble_burst_4,
        R.mipmap.ico_drag_bubble_burst_5
    )


    init {
        val array = getContext().obtainStyledAttributes(attrs, R.styleable.DragBubbleView);
        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius, mBubbleRadius);
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text) ?: ""
        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();
        //两个圆半径大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;
        //抗锯齿
        mBubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mBubbleColor
            style = Paint.Style.FILL
        }
        mBezierPath = Path()
        //文本画笔
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mTextColor
            textSize = mTextSize
        }
        mTextRect = Rect()
        //爆炸画笔
        mBurstPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBurstPaint.isFilterBitmap = true
        mBurstRect = Rect()
        mBurstBitmapsArray = Array(mBurstDrawablesArray.size) {
            BitmapFactory.decodeResource(resources, mBurstDrawablesArray[it])
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        //将不动气泡圆心设置为控件中心位置
        mBubFixedCenter?.set(w / 2f, h / 2f) ?: let {
            mBubFixedCenter = PointF(w / 2f, h / 2f)
        }

        //将可动气泡圆心设置为控件中心位置
        mBubMoveableCenter?.set(w / 2f, h / 2f) ?: let {
            mBubMoveableCenter = PointF(w / 2f, h / 2f)
        }
    }

    // TODO: 2020/11/11 气泡左右滑动时贝塞尔曲线的开始点计算有问题
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas);
        mBubFixedCenter ?: return
        mBubMoveableCenter ?: return

        val bubFixedCenter = mBubFixedCenter!!
        val bubMovableCenter = mBubMoveableCenter!!

        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制不动气泡
            canvas.drawCircle(bubFixedCenter.x, bubFixedCenter.y, mBubFixedRadius, mBubblePaint);
            //绘制贝塞尔曲线
            //控制点坐标
            val iAnchorX = ((bubFixedCenter.x + bubMovableCenter.x) / 2)
            val iAnchorY = ((bubFixedCenter.y + bubMovableCenter.y) / 2)

            val sinTheta = (bubMovableCenter.y - bubFixedCenter.y) / mDist * 1F
            val cosTheta = (bubMovableCenter.x - bubFixedCenter.x) / mDist * 1F

            //B点坐标
            val iBubMovableStartX = bubMovableCenter.x + sinTheta * mBubMovableRadius;
            val iBubMovableStartY = bubMovableCenter.y - cosTheta * mBubMovableRadius;
            //A点坐标
            val iBubFixedEndX = bubFixedCenter.x + mBubFixedRadius * sinTheta;
            val iBubFixedEndY = bubFixedCenter.y + mBubFixedRadius * cosTheta;
            //D点坐标
            val iBubFixedStartX = bubFixedCenter.x - sinTheta * mBubFixedRadius;
            val iBubFixedStartY = bubFixedCenter.y + cosTheta * mBubFixedRadius;
            //C点坐标
            val iBubMovableEndX = bubMovableCenter.x - mBubMovableRadius * sinTheta;
            val iBubMovableEndY = bubMovableCenter.y + mBubMovableRadius * cosTheta;
            with(mBezierPath) {
                reset()
                moveTo(iBubFixedStartX, iBubFixedStartY);
                quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);
                lineTo(iBubMovableStartX, iBubMovableStartY);
                quadTo(iAnchorX, iAnchorY, iBubFixedEndX, iBubFixedEndY);
                close();
            }
            canvas.drawPath(mBezierPath, mBubblePaint);
        }
        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            //绘制气泡加消息数据
            canvas.drawCircle(
                bubMovableCenter.x,
                bubMovableCenter.y,
                mBubMovableRadius,
                mBubblePaint
            );
            //将文本大小放入mTextRect
            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length, mTextRect);
            canvas.drawText(
                mTextStr,
                bubMovableCenter.x - mTextRect.width() / 2f,
                bubMovableCenter.y + mTextRect.height() / 2f,
                mTextPaint
            );
        }
        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.size) {
            //绘制爆炸图片
            mBurstRect.set(
                (bubMovableCenter.x - mBubMovableRadius).toInt(),
                (bubMovableCenter.y - mBubMovableRadius).toInt(),
                (bubMovableCenter.x + mBubMovableRadius).toInt(),
                (bubMovableCenter.y + mBubMovableRadius).toInt()
            );
            canvas.drawBitmap(
                mBurstBitmapsArray[mCurDrawableIndex],
                null,
                mBurstRect,
                mBubblePaint
            );
        }
        //1. 静止状态，一个气泡加消息数据

        //2. 连接状态，一个气泡加消息数据，贝塞尔曲线，本身位置上小球，大小可变

        //3. 分离状态，一个气泡加消息数据

        //4. 消失状态，爆炸效果，气泡及消息数据消失

    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mBubFixedCenter?: return false
        val bubFixedCenter = mBubFixedCenter!!

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    mDist = hypot(event.x - bubFixedCenter.x, event.y -bubFixedCenter.y);
                    //加上MOVE_OFFSET是为了方便拖拽
                    //点圆内时表示可拖拽，状态设置为连接状态，点圆外不做反应
                    mBubbleState = if (mDist < mBubbleRadius + MOVE_OFFSET) {
                        BUBBLE_STATE_CONNECT;
                    } else {
                        BUBBLE_STATE_DEFAULT;
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mBubbleState != BUBBLE_STATE_DEFAULT) {
                    mDist = hypot (event.x - bubFixedCenter.x, event.y -bubFixedCenter.y);
                    mBubMoveableCenter?.also {
                        it.x = event.x
                        it.y = event.y
                    }
                    if (mBubbleState == BUBBLE_STATE_CONNECT) {
                        //当拖拽的距离在指定范围内，那么调整不动气泡半径
                        if (mDist < mMaxDist - MOVE_OFFSET) {
                            mBubFixedRadius = mBubbleRadius - mDist / 8;
                        } else {
                            //拖拽距离超过指定范围，设置为分离状态
                            mBubbleState = BUBBLE_STATE_APART;
                        }
                    }
                }
                invalidate();
            }
            MotionEvent.ACTION_UP -> {
                if (mBubbleState == BUBBLE_STATE_CONNECT) {
                    //橡皮筋回弹效果
                    startBubbleRestAnim();
                } else if (mBubbleState == BUBBLE_STATE_APART) {
                    //若拖动距离越过一定距离才会执行爆炸效果
                    if (mDist < 2 * mBubbleRadius) {
                        startBubbleRestAnim();
                    } else {
                        //爆炸效果
                        startBubbleBurstAnim();
                    }
                }
            }
        }
        return true
    }

    /**
     * 橡皮筋回弹效果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun startBubbleRestAnim() {
        mBubMoveableCenter ?: return
        mBubFixedCenter ?: return
        ValueAnimator.ofObject(
            PointFEvaluator(),
            PointF(mBubMoveableCenter!!.x, mBubMoveableCenter!!.y),
            PointF(mBubFixedCenter!!.x, mBubFixedCenter!!.y)
        ).apply {
            duration = 200
            interpolator = OvershootInterpolator(5F)
            addUpdateListener {
                mBubMoveableCenter = it.animatedValue as PointF
                invalidate()
            }
            addListener(onEnd =  {
                mBubbleState = BUBBLE_STATE_DEFAULT
            })
            start()
        }
    }

    /**
     * 爆炸效果
     */
    private fun startBubbleBurstAnim() {
        mBubbleState = BUBBLE_STATE_DISMISS
        ValueAnimator.ofInt(0, mBurstBitmapsArray.size).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener {
                mCurDrawableIndex = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }
}
