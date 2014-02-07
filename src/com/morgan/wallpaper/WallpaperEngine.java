package com.morgan.wallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.SurfaceHolder;

class WallpaperEngine extends Engine {

    private SurfaceHolder mSurfaceHolder;
    private WallperThread mThread;
    private Context mContext;
    private int index = 0;
    private static final int[] mBitmaps = { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f,
            R.drawable.g, R.drawable.h, R.drawable.i, R.drawable.j };

    public WallpaperEngine(WallpaperService service) {
        service.super();
        Log.e("wallpaper", "初始化引擎");
        mSurfaceHolder = getSurfaceHolder();
        mThread = new WallperThread(mHandler);
        this.mContext = service;
    }

    @Override
    public void onVisibilityChanged(boolean visible)
    {
        Log.e("wallpaper", visible == true ? "转换为可见" : "转换为不可见");
        if (visible) {
            render();
        }
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.e("wallpaper", "SurfaceChanged");
        super.onSurfaceChanged(holder, format, width, height);
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder)
    {
        super.onSurfaceCreated(holder);
        Log.e("wallpaper", "SurfaceCreated");
        mThread.startThread();
    }

    @Override
    public void onSurfaceDestroyed(SurfaceHolder holder)
    {
        Log.e("wallpaper", "SurfaceDestroyed");
        mThread.stopThread();
        super.onSurfaceDestroyed(holder);
    }

    public void render()
    {
        Canvas canvas = null;
        try {
            canvas = this.mSurfaceHolder.lockCanvas(null);
            synchronized (this.mSurfaceHolder) {
                this.draw(canvas);
            }
        } finally {
            if (canvas != null) {
                this.mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void draw(Canvas canvas)
    {
        if (null == canvas) { return; }
        Log.e("wallpaper", "更新界面");
        Bitmap bitmap = BitmapFactory.decodeResource(this.mContext.getResources(), mBitmaps[index]);
        index++;
        index %= mBitmaps.length;
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)canvas.getWidth() / bitmap.getWidth());
        float scaleHeight = ((float)canvas.getHeight() / bitmap.getHeight());
        matrix.postScale(scaleWidth, scaleHeight);
        canvas.drawBitmap(bitmap, matrix, new Paint());
        bitmap.recycle();
        bitmap = null;
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg)
        {
            render();
        };
    };
}
