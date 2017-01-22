package com.perfume.activity;
import android.view.*;
import com.perfume.*;
import net.youmi.android.normal.spot.*;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
/*
启动界面
*/
public class StartActivity extends AppCompatActivity
   {

      @Override
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_start);
            AdManager.getInstance(this).init("305da1e6aeeaf843", "a4eefc48f4474ae8", false, true);
            setupSplashAd();
         }
      private void setupSplashAd() {
            final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
            RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ABOVE, R.id.view_divider);
            SplashViewSettings splashViewSettings = new SplashViewSettings();
            splashViewSettings.setTargetClass(MainActivity.class);
            splashViewSettings.setSplashViewContainer(splashLayout);
            SpotManager.getInstance(this).showSplash(this, splashViewSettings, new SpotListener() {
                     @Override
                     public void onShowSuccess() {
                          logError("开屏展示成功");
                        }
                     @Override
                     public void onShowFailed(int errorCode) {
                          logError("开屏展示失败");
                           switch (errorCode) {
                                 case ErrorCode.NON_NETWORK:
                                   logError("网络异常");
                                    break;
                                 case ErrorCode.NON_AD:
                                    logError("暂无开屏广告");
                                    break;
                                 case ErrorCode.RESOURCE_NOT_READY:
                                  logError("开屏资源还没准备好");
                                    break;
                                 case ErrorCode.SHOW_INTERVAL_LIMITED:
                                    logError("开屏展示间隔限制");
                                    break;
                                 case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                  logError("开屏控件处在不可见状态");
                                    break;
                                 default:
                                   logError(String.valueOf( errorCode));
                                    break;
                              }
                        }
                     @Override
                     public void onSpotClosed() {
                         logError("开屏被关闭");
                        }

                     @Override
                     public void onSpotClicked(boolean isWebPage) {
                       //    logDebug("开屏被点击");
                         //  logInfo("是否是网页广告？%s", isWebPage ? "是" : "不是");
                        }
                  });
         }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            SpotManager.getInstance(this).onDestroy();
         }
         
      private void logError(String i)
      {
         System.out.println(i);
      }
   }


