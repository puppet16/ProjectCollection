package cn.ltt.projectcollection.picparticlessplit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cn.ltt.projectcollection.R;

/**
 * ============================================================
 *
 * @author 李桐桐
 * date    2020/10/10
 * desc    大图会卡
 * ============================================================
 **/
public class ParticlesSplitView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private List<BallBean> mBalls = new ArrayList<>();
    private float d = 3;//粒子直径
    private ValueAnimator mAnimator;

    public ParticlesSplitView(Context context) {
        this(context, null);
    }

    public ParticlesSplitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticlesSplitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ParticlesSplitView);
        int picResId = a.getResourceId(R.styleable.ParticlesSplitView_picResId, -1);
        if (picResId > 0) {
            mBitmap = BitmapFactory.decodeResource(getResources(), picResId);
        }
        a.recycle();
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        analysisPic();
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                updateBall();
                invalidate();
            }
        });
    }

    /**
     * 更新粒子位置
     */
    private void updateBall() {
        for (BallBean ball : mBalls) {
            ball.x += ball.vX;
            ball.y += ball.vY;
            ball.vX += ball.aX;
            ball.vY += ball.aY;
        }
    }

    public void setPicBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        analysisPic();
    }

    /**
     * 将图片解析成一个个粒子
     */
    private void analysisPic() {
        if (null == mBitmap) {
            return;
        }
        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                BallBean ball = new BallBean();
                ball.color = mBitmap.getPixel(i, j);
                ball.x = i * d + d / 2;
                ball.y = j * d + d / 2;
                ball.r = d / 2;
                //速度(-20,20)
                ball.vX = rangInt(-20, 20);
                ball.vY = rangInt(-15, 35);
                ball.aX = 0;
                ball.aY = 0.98f;
                mBalls.add(ball);
            }
        }
    }

    private float rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数，再随机
        return (float) (min + Math.ceil(Math.random() * (max - min)));
    }

    /**
     * 将粒子画到画布上
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBalls.isEmpty()) {
            return;
        }
        canvas.translate(200, 300);
        for (BallBean ball : mBalls) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.r, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }
}
