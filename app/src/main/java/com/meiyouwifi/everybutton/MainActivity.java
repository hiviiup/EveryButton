package com.meiyouwifi.everybutton;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements DownloadButton.OnButtonStatusChangedListener, TimerButton.OnTimerStartListener

{

    private DownloadButton db;
    private TimerButton timerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = (DownloadButton) findViewById(R.id.download_btn);
        db.setOnButtonStatusChangedListener(this);
        db.setStatus(DownloadButton.ButtonStatus.IDLE);

        timerButton = (TimerButton) findViewById(R.id.timebtn);
        timerButton.setOnTimerStartListener(this);
    }


    private float downloadPercent = 0;

    private Handler mHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (downloadPercent < 1000)
            {
                downloadPercent += 10;
                db.setProgress(downloadPercent / 1000f);
                db.setButtonText(String.format("已下载%.2f", downloadPercent/1000f * 100) + "%");
                mHandler.sendEmptyMessageDelayed(0, 100);
            } else
            {
                db.setStatus(DownloadButton.ButtonStatus.DONE);
            }
            return true;
        }
    });

    @Override
    public void buttonStatusChanged(DownloadButton.ButtonStatus status)
    {
        switch (status)
        {
            case DOWNLOAD:
                mHandler.sendEmptyMessage(0);
                break;
            case IDLE:
                db.setButtonText("下载");
                Log.e("MainActivity", "空闲状态");
                break;

            case PAUSE:
                Log.e("MainActivity", "暂停");
                db.setButtonText("暂停");
                break;

            case DONE:
                Log.e("MainActivity", "下载完成");
                db.setButtonText("打开");
                downloadPercent = 0;
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        timerButton.cancel();
    }

    @Override
    public void start()
    {
        Log.e("MainActivity","请求发送验证码");
    }
}
