package cn.ltt.projectcollection.dragbubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
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

import cn.ltt.projectcollection.R;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/11/6
 * desc    仿qq消息气泡
 * ============================================================
 **/
public class DragBubbleView extends View {
    //气泡默认状态-静止
    private final int BUBBLE_STATE_DEFAULT = 0;
    //气泡相连
    private final int BUBBLE_STATE_CONNECT = 1;
    //气泡分离
    private final int BUBBLE_STATE_APART = 2;
    //气泡消失
    private final int BUBBLE_STATE_DISMISS = 3;
    //气泡半径
    private float mBubbleRadius;
    //气泡颜色
    private int mBubbleColor;
    //气泡消息文字
    private String mTextStr;
    //气泡消息文字大小
    private float mTextSize;
    //气泡消息文字颜色
    private int mTextColor;
    //不动气泡的半径
    private float mBubFixedRadius;
    //可动气泡的半径
    private float mBubMovableRadius;
    //不动气泡的圆心
    private PointF mBubFixedCenter;
    //可动气泡圆心
    private PointF mBubMoveableCenter;

    //气泡画笔
    private Paint mBubblePaint;
    private Paint mTextPaint;
    //贝塞尔曲线path
    private Path mBezierPath;
    //文本绘制区域
    private Rect mTextRect;
    private Paint mBurstPaint;
    //爆炸绘制区域
    private Rect mBurstRect;
    //气泡状态标志
    private int mBubbleState = BUBBLE_STATE_DEFAULT;
    //两气泡圆心距离
    private float mDist;
    //气泡相连状态最大圆心距离
    private float mMaxDist;
    //手指触摸偏移量
    private float MOVE_OFFSET;
    //气泡爆炸的bitmap数组
    private Bitmap[] mBurstBitmapsArray;
    //是否在执行气泡爆炸动画
    private boolean mIsBurstAnimStart = false;
    //当前气泡爆炸图片index
    private int mCurDrawableIndex;
    //气泡爆炸的图片数组
    private int[] mBurstDrawablesArray = {R.mipmap.ico_drag_bubble_burst_1, R.mipmap.ico_drag_bubble_burst_2, R.mipmap.ico_drag_bubble_burst_3, R.mipmap.ico_drag_bubble_burst_4, R.mipmap.ico_drag_bubble_burst_5};

    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DragBubbleView);
        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius, mBubbleRadius);
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text);
        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();
        //两个圆半径大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;
        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);

        mBezierPath = new Path();
        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();
        //爆炸画笔
        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBurstPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstDrawablesArray.length; i++) {
            //将气泡爆炸的drawable转为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
            mBurstBitmapsArray[i] = bitmap;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //将不动气泡圆心设置为控件中心位置
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w / 2f, h / 2f);
        } else {
            mBubFixedCenter.set(w / 2f, h / 2f);
        }
        //将可动气泡圆心设置为控件中心位置
        if (mBubMoveableCenter == null) {
            mBubMoveableCenter = new PointF(w / 2f, h / 2f);
        } else {
            mBubMoveableCenter.set(w / 2f, h / 2f);
        }
    }

    // TODO: 2020/11/11 气泡左右滑动时贝塞尔曲线的开始点计算有问题
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制不动气泡
            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, mBubFixedRadius, mBubblePaint);
            //绘制贝塞尔曲线
            //控制点坐标
            int iAnchorX = (int) ((mBubFixedCenter.x + mBubMoveableCenter.x) / 2);
            int iAnchorY = (int) ((mBubFixedCenter.y + mBubMoveableCenter.y) / 2);

            float sinTheta = (mBubMoveableCenter.y - mBubFixedCenter.y) / mDist;
            float cosTheta = (mBubMoveableCenter.x - mBubFixedCenter.x) / mDist;

            //B点坐标
            float iBubMovableStartX = mBubMoveableCenter.x + sinTheta * mBubMovableRadius;
            float iBubMovableStartY = mBubMoveableCenter.y - cosTheta * mBubMovableRadius;
            //A点坐标
            float iBubFixedEndX = mBubFixedCenter.x + mBubFixedRadius * sinTheta;
            float iBubFixedEndY = mBubFixedCenter.y + mBubFixedRadius * cosTheta;
            //D点坐标
            float iBubFixedStartX = mBubFixedCenter.x - sinTheta * mBubFixedRadius;
            float iBubFixedStartY = mBubFixedCenter.y + cosTheta * mBubFixedRadius;
            //C点坐标
            float iBubMovableEndX = mBubMoveableCenter.x - mBubMovableRadius * sinTheta;
            float iBubMovableEndY = mBubMoveableCenter.y + mBubMovableRadius * cosTheta;
            mBezierPath.reset();
            mBezierPath.moveTo(iBubFixedStartX, iBubFixedStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);
            mBezierPath.lineTo(iBubMovableStartX, iBubMovableStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubFixedEndX, iBubFixedEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }
        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            //绘制气泡加消息数据
            canvas.drawCircle(mBubMoveableCenter.x, mBubMoveableCenter.y, mBubMovableRadius, mBubblePaint);
            //将文本大小放入mTextRect
            mTextPaint.getTextBounds(mTextStr, 0, mTextStr.length(), mTextRect);
            canvas.drawText(mTextStr, mBubMoveableCenter.x - mTextRect.width() / 2f, mBubMoveableCenter.y + mTextRect.height() / 2f, mTextPaint);
        }
        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length) {
            //绘制爆炸图片
            mBurstRect.set((int)(mBubMoveableCenter.x - mBubMovableRadius),
                    (int)(mBubMoveableCenter.y - mBubMovableRadius),
                    (int)(mBubMoveableCenter.x + mBubMovableRadius),
                    (int)(mBubMoveableCenter.y + mBubMovableRadius));
            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex], null, mBurstRect, mBubblePaint);
        }
        //1. 静止状态，一个气泡加消息数据

        //2. 连接状态，一个气泡加消息数据，贝塞尔曲线，本身位置上小球，大小可变

        //3. 分离状态，一个气泡加消息数据

        //4. 消失状态，爆炸效果，气泡及消息数据消失

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    //加上MOVE_OFFSET是为了方便拖拽
                    //点圆内时表示可拖拽，状态设置为连接状态，点圆外不做反应
                    if (mDist < mBubbleRadius + MOVE_OFFSET) {
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    } else {
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBubbleState != BUBBLE_STATE_DEFAULT) {
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    mBubMoveableCenter.x = event.getX();
                    mBubMoveableCenter.y = event.getY();
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
                break;
            case MotionEvent.ACTION_UP:
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
                break;
        }
        return true;
    }

    /**
     * 橡皮筋回弹效果
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startBubbleRestAnim() {
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubMoveableCenter.x, mBubMoveableCenter.y),
                new PointF(mBubFixedCenter.x, mBubFixedCenter.y));
        animator.setDuration(200);
        animator.setInterpolator(new OvershootInterpolator(5f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBubMoveableCenter = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBubbleState = BUBBLE_STATE_DEFAULT;

            }
        });
        animator.start();
    }

    /**
     * 爆炸效果
     */
    private void startBubbleBurstAnim() {
        mBubbleState = BUBBLE_STATE_DISMISS;
        ValueAnimator animator = ValueAnimator.ofInt(0, mBurstBitmapsArray.length);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurDrawableIndex = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}
