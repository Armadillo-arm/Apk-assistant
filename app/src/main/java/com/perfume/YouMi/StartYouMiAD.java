package com.perfume.YouMi;
import net.youmi.android.normal.spot.*;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.perfume.R;
import net.youmi.android.normal.common.ErrorCode;
/*
插屏广告
*/
public class StartYouMiAD
   {
      public static void HideSpot ( Context thzz )
         {
            if ( SpotManager.getInstance ( thzz ).isSpotShowing ( ) )
               {
                  SpotManager.getInstance ( thzz ).hideSpot ( );
               } 
         }
      public static void ShowSpot ( Context thzz )
         {
            SpotManager.getInstance ( thzz ).setImageType ( SpotManager.IMAGE_TYPE_VERTICAL );
            SpotManager.getInstance ( thzz ).setAnimationType ( SpotManager.ANIMATION_TYPE_ADVANCED );
            SpotManager.getInstance ( thzz ).showSpot ( thzz, new SpotListener ( ) {
                     @Override
                     public void onShowSuccess ( )
                        {
                           LogUtis.Log ( "插屏展示成功" );
                        }
                     @Override
                     public void onShowFailed ( int errorCode )
                        {
                           LogUtis.Log ( "插屏展示失败" );
                           switch ( errorCode )
                              {
                                 case ErrorCode.NON_NETWORK:
                                    LogUtis.Log ( "网络异常" );
                                    break;
                                 case ErrorCode.NON_AD:
                                    LogUtis.Log ( "暂无插屏广告" );
                                    break;
                                 case ErrorCode.RESOURCE_NOT_READY:
                                    LogUtis.Log ( "插屏资源还没准备好" );
                                    break;
                                 case ErrorCode.SHOW_INTERVAL_LIMITED:
                                    LogUtis.Log ( "请勿频繁展示" );
                                    break;
                                 case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                    LogUtis.Log ( "请设置插屏为可见状态" );
                                    break;
                                 default:
                                    LogUtis.Log ( "请稍后再试" );
                                    break;
                              }
                        }
                     @Override
                     public void onSpotClosed ( )
                        {
                           LogUtis.Log ( "插屏被关闭" );
                        }
                     @Override
                     public void onSpotClicked ( boolean isWebPage )
                        {
                           LogUtis.Log ( "插屏被点击" );
                           LogUtis.Log ( "是否是网页广告？%s", isWebPage ? "是" : "不是" );
                        }
                  } );
         }
      public static void onPause ( Context thzz )
         {
            SpotManager.getInstance ( thzz ).onPause ( );
         }
      public static  void onStop ( Context thzz )
         {
            SpotManager.getInstance ( thzz ).onStop ( );
         }
      public static  void onDestroy ( Context thzz )
         {
            SpotManager.getInstance ( thzz ).onDestroy ( );
         }
   }
