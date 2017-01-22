package com.perfume.YouMi;

import net.youmi.android.normal.spot.*;

import android.view.View;
import android.widget.Button;
import com.perfume.R;
import net.youmi.android.normal.common.ErrorCode;
import android.content.Context;
/*
插屏广告
*/
public class StartSlideableSpotAd
   {
      public static void ShowSpot ( Context mContext )
         {
            SpotManager.getInstance ( mContext ).setImageType ( SpotManager.IMAGE_TYPE_VERTICAL );
            SpotManager.getInstance ( mContext ).setAnimationType ( SpotManager.ANIMATION_TYPE_ADVANCED );
            SpotManager.getInstance ( mContext ).showSlideableSpot ( mContext, new SpotListener ( ) {
                     @Override
                     public void onShowSuccess ( )
                        {
                           LogUtis.Log ( "轮播插屏展示成功" );
                        }
                     @Override
                     public void onShowFailed ( int errorCode )
                        {
                           LogUtis.Log ( "轮播插屏展示失败" );
                           switch ( errorCode )
                              {
                                 case ErrorCode.NON_NETWORK:
                                    LogUtis.Log ( "网络异常" );
                                    break;
                                 case ErrorCode.NON_AD:
                                    LogUtis.Log ( "暂无轮播插屏广告" );
                                    break;
                                 case ErrorCode.RESOURCE_NOT_READY:
                                    LogUtis.Log ( "轮播插屏资源还没准备好" );
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
                           LogUtis.Log ( "轮播插屏被关闭" );
                        }
                     @Override
                     public void onSpotClicked ( boolean isWebPage )
                        {
                           LogUtis.Log ( "轮播插屏被点击" );
                           LogUtis.Log ( "是否是网页广告？%s", isWebPage ? "是" : "不是" );
                        }
                  } );
         }
      public static void HideSpot ( Context mContext )
         {
            if ( SpotManager.getInstance ( mContext ).isSlideableSpotShowing ( ) )
               {
                  SpotManager.getInstance ( mContext ).hideSlideableSpot ( );
               }
         }
      public static void onPause ( Context mContext )
         {
            SpotManager.getInstance ( mContext ).onPause ( );
         }
      public static void onStop ( Context mContext )
         {
            SpotManager.getInstance ( mContext ).onStop ( );
         }
      public static void onDestroy ( Context mContext )
         {
            SpotManager.getInstance ( mContext ).onDestroy ( );
         }
   }

