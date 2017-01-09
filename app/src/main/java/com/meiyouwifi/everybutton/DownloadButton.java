package com.meiyouwifi.everybutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hiviiup on 17/1/6.
 * 下载按钮。需要实现以下功能
 * 1.点击进行下载,按钮状态不可点击
 * 2.根据下载进度,设置按钮背景,并提示下载进度
 * 3.下载完成,改变按钮可点击状态
 */
public class DownloadButton extends TextView implements View.OnClickListener
{

    private Paint paint;
    private RectF rectF;
    private ButtonStatus currentStatus = ButtonStatus.IDLE;
    private int buttonDownloadColor = 0xff31ac9f;
    private int buttonDownloadBackColor = 0xffe9e9e9;
    private int buttonDownloadDoneColor = 0xffec8f4b;

    public enum ButtonStatus
    {
        IDLE,
        DOWNLOAD,
        PAUSE,
        DONE
    }

    //状态改变监听器
    private OnButtonStatusChangedListener onButtonStatusChangedListener;

    public interface OnButtonStatusChangedListener
    {
        void buttonStatusChanged(ButtonStatus status);
    }

    public void setOnButtonStatusChangedListener(OnButtonStatusChangedListener listener)
    {
        this.onButtonStatusChangedListener = listener;
    }


    public DownloadButton(Context context)
    {
        this(context, null);
    }

    public DownloadButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DownloadButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        rectF = new RectF();
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        rectF.left = rectF.top = 0;
        rectF.right = getMeasuredWidth();
        rectF.bottom = getMeasuredHeight();

        int radiusY;
        int radiusX = radiusY = Math.min(getMeasuredHeight(), getMeasuredWidth()) / 4;


        switch (currentStatus)
        {
            case IDLE:
                paint.setColor(buttonDownloadColor);
                canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
                break;
            case DOWNLOAD:
                //绘制进度条背景
                paint.setColor(buttonDownloadBackColor);
                canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
                //绘制进度条
                rectF.right = getMeasuredWidth() * progress;
                paint.setColor(buttonDownloadColor);
                canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
                break;

            case PAUSE:
                //暂停进度条

                break;

            case DONE:
                paint.setColor(buttonDownloadDoneColor);
                rectF.right = getMeasuredWidth();
                canvas.drawRoundRect(rectF, radiusX, radiusY, paint);
                break;
        }

        //获取字体的长度
        paint.setColor(0xff000000);
        paint.setTextSize(getMeasuredHeight() * 0.3f);

        Rect textRect = new Rect();
        paint.getTextBounds(buttonText, 1, buttonText.length(), textRect);
        int textWidth = textRect.width();
        int textHeight = textRect.height();

        canvas.drawText(buttonText, getMeasuredWidth() / 2 - textWidth / 2,
                rectF.bottom / 2 + textHeight / 2, paint);


    }

    /**
     * 设置状态
     *
     * @param status
     */
    public void setStatus(ButtonStatus status)
    {
        this.currentStatus = status;
        onButtonStatusChangedListener.buttonStatusChanged(status);
        postInvalidate();
    }

    private String buttonText;

    public void setButtonText(CharSequence text)
    {
        this.buttonText = (String) text;
        postInvalidate();
    }

    private float progress;

    /**
     * 设置下载进度
     *
     * @param downloadPercent
     */
    public void setProgress(float downloadPercent)
    {

        this.progress = downloadPercent;
        postInvalidate();
    }

    @Override
    public void onClick(View v)
    {
        switch (currentStatus)
        {
            case IDLE:
                setStatus(ButtonStatus.DOWNLOAD);
                break;
            case DOWNLOAD:
                setStatus(ButtonStatus.PAUSE);
                break;
            case PAUSE:
                setStatus(ButtonStatus.DOWNLOAD);
                break;
            case DONE:
                break;
        }
    }
}
