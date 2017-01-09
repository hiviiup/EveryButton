package com.meiyouwifi.everybutton;

import android.content.Context;
import android.graphics.Color;
import android.icu.util.TimeUnit;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hiviiup on 17/1/6.
 * 计时器,用于接收验证码的时候
 */

public class TimerButton extends TextView implements View.OnClickListener
{

    private TimeCount timeCount;

    private OnTimerStartListener listener;

    public interface OnTimerStartListener
    {
        void start();
    }

    public void setOnTimerStartListener(OnTimerStartListener listener)
    {
        this.listener = listener;
    }

    private int defaultBackColor = 0xff31ac9f;
    private int unClickableBackColor = 0xff999999;

    public TimerButton(Context context)
    {
        this(context, null);
    }

    public TimerButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TimerButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        setGravity(Gravity.CENTER);
        setBackgroundColor(defaultBackColor);
        setText("验证码");
    }

    //default
    private long millisInFuture = 60 * 1000, countDownInterval = 1000;

    public void setUnit(long millisInFuture, long countDownInterval)
    {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;

        timeCount = new TimeCount(millisInFuture, countDownInterval);
        setOnClickListener(this);
    }

    public void cancel()
    {
        if (timeCount != null)
            timeCount.cancel();
    }

    @Override
    public void onClick(View v)
    {
        timeCount.start();
        listener.start();
    }

    private class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            setBackgroundColor(unClickableBackColor);
            setClickable(false);
            setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish()
        {
            setClickable(true);
            setText("发送");
            setBackgroundColor(defaultBackColor);
        }
    }
}
