package com.wind.carmanager.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wind.carmanager.R;

/**
 * Created by W010003373 on 2018/7/9.
 */

public class CirclePercentBar extends View {
    private Context mContext;

    private int mArcColor;
    private int mArcWidth;
    private int mCenterTextColor;
    private int mCenterTextSize;
    private int mDescbTextColor;
    private int mDescbTextSize;
    private int mCircleRadius;
    private Paint arcPaint;
    private Paint arcCirclePaint;
    private Paint centerTextPaint;
    private Paint descbTextPaint;
    private RectF arcRectF;
    private Rect textBoundRect;
    private Rect descbBoundRect;
    private float mCurData=30;
    private int arcStartColor;
    private Paint startCirclePaint;
    private String centerText;
    private String descbText;

    public CirclePercentBar(Context context) {
       this(context, null);
    }

    public CirclePercentBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentBar, defStyleAttr, 0);
        mArcColor = typedArray.getColor(R.styleable.CirclePercentBar_arcColor, 0x0000ff);
        mArcWidth = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_arcWidth, 10);
        mCenterTextColor = typedArray.getColor(R.styleable.CirclePercentBar_centerTextColor, 0x0000ff);
        mCenterTextSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_centerTextSize, 40);
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_circleRadius, 150);
        arcStartColor = typedArray.getColor(R.styleable.CirclePercentBar_arcStartColor, 0x0000ff);
        mDescbTextColor = typedArray.getColor(R.styleable.CirclePercentBar_descbTextColor, 0x0000ff);
        mDescbTextSize = typedArray.getDimensionPixelSize(R.styleable.CirclePercentBar_descbTextSize, 40);

        typedArray.recycle();
        initPaint();
    }

    private void initPaint(){
        startCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        startCirclePaint.setStyle(Paint.Style.FILL);
        startCirclePaint.setStrokeWidth(SupportMultipleScreensUtil.getScaleValue(mArcWidth));
        startCirclePaint.setColor(arcStartColor);

        arcCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcCirclePaint.setStyle(Paint.Style.STROKE);
        arcCirclePaint.setStrokeWidth(SupportMultipleScreensUtil.getScaleValue(mArcWidth));
        arcCirclePaint.setColor(mArcColor);
        arcCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(SupportMultipleScreensUtil.getScaleValue(mArcWidth));
        arcPaint.setColor(arcStartColor);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        centerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerTextPaint.setStyle(Paint.Style.STROKE);
        centerTextPaint.setColor(mCenterTextColor);
        centerTextPaint.setTextSize(SupportMultipleScreensUtil.getScaleValue(mCenterTextSize));

        descbTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        descbTextPaint.setStyle(Paint.Style.STROKE);
        descbTextPaint.setColor(mDescbTextColor);
        descbTextPaint.setTextSize(SupportMultipleScreensUtil.getScaleValue(mDescbTextSize));

        arcRectF = new RectF();
        textBoundRect = new Rect();
        descbBoundRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureDimension(widthMeasureSpec),measureDimension(heightMeasureSpec));
    }

    private int measureDimension(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = SupportMultipleScreensUtil.getScaleValue(mCircleRadius)*2;
            if(specMode==MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90, getWidth()/2, getHeight()/2);

        arcRectF.set(getWidth()/2 - SupportMultipleScreensUtil.getScaleValue(mCircleRadius) + SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2, getHeight()/2 - SupportMultipleScreensUtil.getScaleValue(mCircleRadius) + SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2,
                      getWidth()/2 + SupportMultipleScreensUtil.getScaleValue(mCircleRadius) - SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2, getHeight()/2 + SupportMultipleScreensUtil.getScaleValue(mCircleRadius) - SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2);

        canvas.drawArc(arcRectF, 0, 360, false, arcCirclePaint);

        canvas.drawArc(arcRectF, 0 , 360*mCurData/100, false, arcPaint);

        canvas.rotate(90, getWidth()/2, getHeight()/2);
        canvas.drawCircle(getWidth()/2, getHeight()/2 - SupportMultipleScreensUtil.getScaleValue(mCircleRadius) + SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2, SupportMultipleScreensUtil.getScaleValue(mArcWidth)/2, startCirclePaint);

        centerTextPaint.getTextBounds(centerText, 0, centerText.length(), textBoundRect);
        canvas.drawText(centerText, getWidth()/2 - textBoundRect.width()/2, getHeight()/2 + textBoundRect.height()/2, centerTextPaint);

        descbTextPaint.getTextBounds(descbText, 0, descbText.length(), descbBoundRect);
        canvas.drawText(descbText, getWidth()/2 - descbBoundRect.width()/2, getHeight()/2 + textBoundRect.height()/2 + 40, descbTextPaint);
    }

    public void setPercentBattery(float data, TimeInterpolator interpolator){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(mCurData,data);
        valueAnimator.setDuration((long) (Math.abs(mCurData-data)*30));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                mCurData=(float)(Math.round(value*10))/10;
                centerText = String.valueOf(mCurData) + "%";
                descbText = "剩余电量";
                invalidate();
            }
        });
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }

  /*  public void setPercentMileage(float data, TimeInterpolator interpolator){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(mCurData,data);
        valueAnimator.setDuration((long) (Math.abs(mCurData-data)*30));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();
                mCurData=(float)(Math.round(value*10))/10;
                centerText = String.valueOf(mCurData) + "km";
                descbText = "累计里程数";
                invalidate();
            }
        });
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }*/
}
