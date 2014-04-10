package com.morgan.wallpaper;

import android.os.Handler;
import android.util.Log;

public class WallperThread extends Thread {

    private boolean mCanRunning;
    private Handler mHandler ;

    public WallperThread(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void startThread(){
        Log.e("wallpaper", "启动更新线程");
        this.mCanRunning = true;
        this.start();
    }

    public void stopThread()
    {
        Log.e("wallpaper", "结束更新线程");
        this.mCanRunning = false;
        synchronized(this){
            this.notify();
        }
        boolean retry = true;
        while (retry) {
            try {
             this.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }

    @Override
    public void run()
    {
        while (mCanRunning) {
            try {
                //每15秒切换一张壁纸
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(0);
        }
    }
}
