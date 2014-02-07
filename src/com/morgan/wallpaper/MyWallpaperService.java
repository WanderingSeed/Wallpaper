package com.morgan.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.util.Log;

public class MyWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine()
    {
        Log.e("wallpaper", "Service创建");
        return new WallpaperEngine(this);
    }

}
