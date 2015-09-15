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

    private int mCenterX;
    private int mCenterY;

    private float mDensity;
    private Paint mFillPaint;
    private Path mPath = new Path();
    private Path mPath2 = new Path();

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

    private void setInitPaint(){
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        final int sizeW = Math.round(w * 0.3f);
        final int sizeH = Math.round(h * 0.3f);
        final int gap = Math.round(h * 0.1f);

        mPath.reset();
        mPath.moveTo(mCenterX , mCenterY - sizeH - gap); //頂点
        mPath.lineTo(mCenterX + sizeW, mCenterY + sizeH - gap); //右下
        mPath.lineTo(mCenterX - sizeW, mCenterY + sizeH - gap); //左下
        mPath.close();

        mPath2.reset();
        mPath2.moveTo(mCenterX, mCenterY + sizeH + gap);
        mPath2.lineTo(mCenterX + sizeW, mCenterY - sizeH + gap);
        mPath2.lineTo(mCenterX - sizeW, mCenterY - sizeH + gap);
        mPath2.close();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }

    private void drawView(Canvas canvas){
        canvas.drawPath(mPath,mFillPaint);
        canvas.drawPath(mPath2,mFillPaint);
    }
}