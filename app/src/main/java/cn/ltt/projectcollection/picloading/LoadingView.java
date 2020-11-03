package cn.ltt.projectcollection.picloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import cn.ltt.projectcollection.R;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/26
 * desc    旋转水波纹加载
 * ============================================================
 **/
public class LoadingView extends View {

    //旋转圆的画笔，六个小球的
    private Paint mPaint;
    //水波纹扩散圆画笔
    private Paint mHolePaint;
    //属性动画
    private ValueAnimator mValueAnimator;
    //背景色
    private int mBackgroundColor = Color.WHITE;
    //六个小球颜色
    private int[] mCircleColors;

    //旋转圆的中心坐标
    private float mCenterX;
    private float mCenterY;
    //水波纹扩散半径
    private float mDistance;

    //6个小球的半径
    private float mCircleRadius = 18;
    //旋转圆的半径
    private float mRotateRadius = 90;
    //当前旋转圆的旋转角度
    private float mCurrentRotateAngle = 0F;
    //当前旋转圆半径
    private float mCurrentRotateRadius = mRotateRadius;
    //扩散圆的半径
    private float mCurrentHoleRadius = 0F;
    //旋转动画时长
    private int mRotateDuration = 1200;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mPaint = new Paint();
        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackgroundColor);
        mCircleColors = getContext().getResources().getIntArray(R.array.picture_loading_circle_colors);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1F / 2;
        mCenterY = h * 1F / 2;
        mDistance = (float)(Math.hypot(w,h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLoadingState == null) {
            mLoadingState = new RotateState();
        }
        mLoadingState.drawState(canvas);
    }

    private LoadingState mLoadingState;

    private static abstract class LoadingState {
        abstract void drawState(Canvas canvas);
    }


    //1. 旋转
    private class RotateState extends LoadingState {
        private RotateState() {
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentRotateAngle = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new MerginState();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制6个小球
            drawCircles(canvas);
        }
    }

    private void drawBackground(Canvas canvas) {
        if (mCurrentHoleRadius > 0) {
            //绘制空心圆
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = mDistance;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX, mCenterY, radius, mHolePaint);
        } else {
            //绘制纯色背景
             canvas.drawColor(mBackgroundColor);
        }
    }

    private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) (Math.PI * 2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            //x = r *cos(a) +centerX
            //y = r * sin(a) + centerY
            float angle = i * rotateAngle + mCurrentRotateAngle;
            float cx = (float) (Math.cos(angle) * mCurrentRotateRadius + mCenterX);
            float cy = (float) (Math.sin(angle) * mCurrentRotateRadius +mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);

        }
    }

    //2. 扩散聚合
    private class MerginState extends LoadingState {

        private MerginState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentRotateRadius = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    //3. 水波纹
    private class ExpandState extends LoadingState {

        private ExpandState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mCurrentHoleRadius = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });

            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }
}
