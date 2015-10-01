package com.example.okamoto_kazuya.fortuneteller.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.okamoto_kazuya.fortuneteller.R;

/**
 * Created by okamoto_kazuya on 15/09/15.
 */
public class MyCustomView extends View {

    private int mFillColor = Color.WHITE;

    private int mSizeW;
    private int mSizeH;
    private int mCenterX;
    private int mCenterY;

    private float mDensity;
    private Paint mFillPaint;
    private Path mPath = new Path();


    public MyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        mDensity = getContext().getResources().getDisplayMetrics().density;
        if (attrs == null) {
            setInitPaint();
            return;
        }

        TypedArray tArray =
                context.obtainStyledAttributes(
                        attrs,
                        R.styleable.MyCustomView
                );

        mFillColor = tArray.getColor(R.styleable.MyCustomView_fill_color, mFillColor);

        setInitPaint();
    }

    public void setData(int money, int job, int love){
        mPath.reset();

        final float maxvalue = 5.0f;
        final float moneyWeighting = money / maxvalue;
        final float jobWeighting = job / maxvalue;
        final float loveWeighting = love / maxvalue;

        mPath.moveTo(mCenterX * moneyWeighting, (mCenterY - mSizeH) * moneyWeighting); //頂点
        mPath.lineTo((mCenterX + mSizeW) * jobWeighting, (mCenterY + mSizeH) * jobWeighting); //右下
        mPath.lineTo((mCenterX - mSizeW) * loveWeighting, (mCenterY + mSizeH) * loveWeighting); //左下
        mPath.close();
    }

    private void setInitPaint(){
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSizeW = Math.round(w * 0.3f);
        mSizeH = Math.round(h * 0.3f);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas){
        canvas.drawPath(mPath,mFillPaint);
    }
}