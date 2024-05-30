package net.onest.time;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MyCounter extends View {
    //圆轮颜色
    private int mRingColor;
    //默认圆颜色
    private int mRingNormalColor ;
    //圆轮宽度
    private float mRingWidth;
    //圆轮进度值文本大小
    private int mRingProgressTextSize;
    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    private Paint mPaint;
    private Paint paintNormal;
    //圆环的矩形区域
    private RectF mRectF;

    private String text;

    private CountDownTimer countDownTimer;
    //
    private int mProgressTextColor;
    private int mCountdownTime;
    private float mCurrentProgress;
    private OnCountDownFinishListener mListener;
    private ValueAnimator valueAnimator ;

    public MyCounter(Context context) {
        this(context, null);
    }

    public MyCounter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        mRingColor = a.getColor(R.styleable.CountDownView_ringColor, Color.BLACK);
        mRingWidth = a.getFloat(R.styleable.CountDownView_ringWidth, 8);
        mRingProgressTextSize = a.getDimensionPixelSize(R.styleable.CountDownView_progressTextSize, dip2px(context, 12));
        mProgressTextColor = a.getColor(R.styleable.CountDownView_progressTextColor, Color.BLUE);
        mCountdownTime = a.getInteger(R.styleable.CountDownView_countdownTime, 60);
        mRingNormalColor = a.getColor(R.styleable.CountDownView_ringColor, Color.RED);
        a.recycle();
        paintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintNormal.setAntiAlias(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        this.setWillNotDraw(false);
    }

    //设置初始倒计时数
    public void setCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRectF = new RectF(0 + mRingWidth / 2, 0 + mRingWidth / 2,
                mWidth - mRingWidth / 2, mHeight - mRingWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         *圆环
         */
        //颜色
        mPaint.setColor(mRingColor);
        //空心
        mPaint.setStyle(Paint.Style.STROKE);
        //宽度
        mPaint.setStrokeWidth(mRingWidth);
        /**
         *默认圆环
         */
        //颜色
        paintNormal.setColor(mRingNormalColor);
        //空心
        paintNormal.setStyle(Paint.Style.STROKE);
        //宽度
        paintNormal.setStrokeWidth(mRingWidth);
        canvas.drawArc(mRectF, 360, 360, false, paintNormal);
        canvas.drawArc(mRectF, -90, mCurrentProgress - 360, false, mPaint);
        //绘制文本
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

//        //转换格式: 00:00
//        NumberFormat f = new DecimalFormat("00");
//        int minute = (mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime)/60)/60;
//        int secend = (mCountdownTime - (int) (mCurrentProgress / 360f * mCountdownTime)/60)%60;
//        String text =f.format(minute)+":"+f.format(secend);


        textPaint.setTextSize(mRingProgressTextSize);
        textPaint.setColor(mProgressTextColor);

        //文字居中显示
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(text, mRectF.centerX(), baseline, textPaint);
    }

    //设置动画【圆环的倒计时时间】
    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }
    /**
     * 开始倒计时
     */
    public void startCountDown() {
        setClickable(false);
        valueAnimator = getValA(mCountdownTime * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                mCurrentProgress = (int) (360 * (i / 100f));
                setTextFromAngle(mCurrentProgress);
                invalidate();
            }
        });
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //倒计时结束回调
                if (mListener != null) {
                    mListener.countDownFinished();
                }
                setClickable(true);
            }
        });
    }

    //倒计时结束，调用接口
    public void setAddCountDownListener(OnCountDownFinishListener mListener) {
        this.mListener = mListener;
    }
    public interface OnCountDownFinishListener {
        void countDownFinished();
    }

    //停止计时
    public void stopCountDown(){
        valueAnimator.end();
    }

    //设置文本格式,并且进行倒计时:
    private void setTextFromAngle(float i) {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };

//        int temp = angleValue / 60;
//        String minute = temp >= 10 ? String.valueOf(temp) : "0" + temp;
//
//        temp = angleValue % (60);
//        String second = temp >= 10 ? String.valueOf(temp) : "0" + temp;
//        this.text = minute + ":" + second;
        NumberFormat f = new DecimalFormat("00");
        int minute = (mCountdownTime - (int) (i / 360f * mCountdownTime)/60)/60;
        int secend = (mCountdownTime - (int) (i / 360f * mCountdownTime)/60)%60;
        this.text =f.format(minute)+":"+f.format(secend);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
