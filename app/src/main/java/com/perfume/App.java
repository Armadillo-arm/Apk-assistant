package com.perfume;

import com.maxleap.*;
import com.nostra13.universalimageloader.core.*;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.perfume.Model.LoginUser;
import com.perfume.Panda.StartAD;
import com.perfume.Utis.CrashCatch;
import com.yun.play.APP;
/*
全局启动类
*/
public class App extends Application
   {
      public volatile static Context thzz;
      @Override
      public void onCreate ( )
         {
            super.onCreate ( );
            thzz=getApplicationContext();
            CrashCatch.getInstance().init(thzz);
            StartAD.Run(this);
            MaxLeap.Options options = new MaxLeap.Options();
            options.applicationID = APP.b(1);
            options.restAPIKey = APP.b(2);
            options.enableAnonymousUser = true;
            options.serverRegion = MaxLeap.REGION_CN;
            MaxLeap.setLogLevel(MaxLeap.LOG_LEVEL_VERBOSE);
            MLUser.registerSubclass(LoginUser.class);
            MaxLeap.initialize(this, options);
            initImageLoader();
            
            MaxLeap.checkSDKConnection();
         }

      private void initImageLoader() {

            ImageLoaderConfiguration mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this)

               .diskCache(new UnlimitedDiskCache(getExternalCacheDir()))
               .diskCacheExtraOptions(480, 800, null)
               .diskCacheSize(100 * 1024 * 1024)
               .diskCacheFileCount(500)
               .diskCacheFileNameGenerator(new Md5FileNameGenerator())

               .memoryCacheExtraOptions(480, 800)
               .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
               .memoryCacheSize(8 * 1024 * 1024)

               .threadPoolSize(5)

               .threadPriority(Thread.NORM_PRIORITY - 2)

               .denyCacheImageMultipleSizesInMemory()

               .tasksProcessingOrder(QueueProcessingType.LIFO)

               .defaultDisplayImageOptions(DisplayImageOptions.createSimple())

               .imageDownloader(new BaseImageDownloader(this, 10 * 1000, 60 * 1000))

               .imageDecoder(new BaseImageDecoder(false))

               .build();

            ImageLoader.getInstance().init(mImageLoaderConfiguration);// 全局初始化此配置

         }

      
}
