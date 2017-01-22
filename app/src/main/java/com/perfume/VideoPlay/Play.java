package com.perfume.VideoPlay;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.baidu.cyberplayer.core.BVideoView.*;
import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Process;
import com.baidu.cyberplayer.core.BVideoView;
import com.perfume.R;
import android.support.v7.app.AppCompatActivity;
/*
播放主窗口
*/
public class Play extends AppCompatActivity implements OnPreparedListener, OnCompletionListener, OnErrorListener,
OnInfoListener, OnPlayingBufferCacheListener, OnCompletionWithParamListener
   {
      private String ak = "52fb568ab9c145ed9f7320ced916cbec"; 
      private BVideoView mVV = null;
      private MediaVideo mediaController = null;
      private RelativeLayout mViewHolder = null;
      private EventHandler mEventHandler;
      private HandlerThread mHandlerThread;
      private final Object syncPlaying = new Object ( );
      private volatile boolean isReadyForQuit = true;
      private Timer barTimer;
      private static final int UI_EVENT_PLAY = 0;
      private long mExitTime;
      public static String PLAY_URL="URL";
      public static String PLAY_COOKIC="COOKIC";
      public enum PlayerStatus
         {
            PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETED
            }
      private PlayerStatus mPlayerStatus = PlayerStatus.PLAYER_IDLE;
      private int mLastPos = 0;
      class EventHandler extends Handler
         {
            public EventHandler ( Looper looper )
               {
                  super ( looper );
               }
            @Override
            public void handleMessage ( Message msg )
               {
                  switch ( msg.what )
                     {
                        case UI_EVENT_PLAY:
                           if ( mPlayerStatus == PlayerStatus.PLAYER_PREPARING || mPlayerStatus == PlayerStatus.PLAYER_PREPARED )
                              {
                                 synchronized ( syncPlaying )
                                    {
                                       try
                                          {
                                             syncPlaying.wait ( 2 * 1000 );
                                          }
                                       catch (InterruptedException e)
                                          {
                                             e.printStackTrace ( );
                                          }
                                    }
                              }
                           Intent intent = getIntent ( );
                           String PlayUrl = intent.getStringExtra ( PLAY_URL );
                           String PlayCookic = intent.getStringExtra ( PLAY_COOKIC );
                           mVV.setCustomHttpHeader ( "Cookie: FTN5K=" + PlayCookic + "\r\n" );
                           mVV.setVideoPath ( PlayUrl );
                           mVV.setVideoScalingMode ( BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT );
                           if ( mLastPos > 0 )
                              {

                                 mVV.seekTo ( mLastPos );
                                 mLastPos = 0;
                              }
                           mVV.showCacheInfo ( true );
                           mVV.start ( );
                           isReadyForQuit = false;
                           changeStatus ( PlayerStatus.PLAYER_PREPARING );
                           break;
                        default:
                           break;
                     }
               }
         }

      @Override
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            getWindow ( ).setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
            getWindow ( ).addFlags ( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
            setContentView ( R.layout.activity_simple_video_playing );
            initUI ( );
            mHandlerThread = new HandlerThread ( "event handler thread", Process.THREAD_PRIORITY_BACKGROUND );
            mHandlerThread.start ( );
            mEventHandler = new EventHandler ( mHandlerThread.getLooper ( ) );
         }
      private void initUI ( )
         {
            mViewHolder = (RelativeLayout) findViewById ( R.id.view_holder );
            mediaController = (MediaVideo) findViewById ( R.id.media_controller_bar );
            BVideoView.setAK ( ak );
            mVV = new BVideoView ( this );
            mVV.setLogLevel ( 0 );
            mViewHolder.addView ( mVV );
            mVV.setOnPreparedListener ( this );
            mVV.setOnCompletionListener ( this );
            mVV.setOnCompletionWithParamListener ( this );
            mVV.setOnErrorListener ( this );
            mVV.setOnInfoListener ( this );
            mediaController.setMediaPlayerControl ( mVV );
            mVV.setDecodeMode ( BVideoView.DECODE_SW );
            mVV.selectResolutionType ( BVideoView.RESOLUTION_TYPE_AUTO );
         }

      @Override
      protected void onPause ( )
         {
            super.onPause ( );

            if ( mVV.isPlaying ( ) && ( mPlayerStatus != PlayerStatus.PLAYER_IDLE ) )
               {
                  mLastPos = (int) mVV.getCurrentPosition ( );
                  mVV.pause ( );
               }
         }

      @Override
      protected void onResume ( )
         {
            super.onResume ( );
            if ( !mVV.isPlaying ( ) && ( mPlayerStatus != PlayerStatus.PLAYER_IDLE ) )
               {
                  mVV.resume ( );
               }
            else
               {
                  mEventHandler.sendEmptyMessage ( UI_EVENT_PLAY );
               }
         }

      @Override
      protected void onStop ( )
         {
            super.onStop ( );
            if ( mVV.isPlaying ( ) && ( mPlayerStatus != PlayerStatus.PLAYER_IDLE ) )
               {
                  mLastPos = (int) mVV.getCurrentPosition ( );
                  mVV.pause ( );
               }
         }

      @Override
      protected void onDestroy ( )
         {
            super.onDestroy ( );
            if ( ( mPlayerStatus != PlayerStatus.PLAYER_IDLE ) )
               {
                  mLastPos = (int) mVV.getCurrentPosition ( );
                  mVV.stopPlayback ( );
               }

            mHandlerThread.quit ( );
            synchronized ( syncPlaying )
               {
                  try
                     {
                        if ( !isReadyForQuit )
                           {
                              syncPlaying.wait ( 2 * 1000 );
                           }
                     }
                  catch (InterruptedException e)
                     {
                        e.printStackTrace ( );
                     }
               }
         }

      @Override
      public boolean onInfo ( int what, int extra )
         {
            switch ( what )
               {
                  case BVideoView.MEDIA_INFO_BUFFERING_START:
                     break;
                  case BVideoView.MEDIA_INFO_BUFFERING_END:
                     break;
                  default:
                     break;
               }
            return false;
         }
      @Override
      public void onPlayingBufferCache ( int percent )
         {
         }
      @Override
      public boolean onError ( int what, int extra )
         {
            synchronized ( syncPlaying )
               {
                  isReadyForQuit = true;
                  syncPlaying.notifyAll ( );
               }
            changeStatus ( PlayerStatus.PLAYER_IDLE );
            return true;
         }
      @Override
      public void onCompletion ( )
         {
            synchronized ( syncPlaying )
               {
                  isReadyForQuit = true;
                  syncPlaying.notifyAll ( );
                  finish ( );
               }
            changeStatus ( PlayerStatus.PLAYER_COMPLETED );
         }
      @Override
      public void onPrepared ( )
         {
            hideOuterAfterFiveSeconds ( );
            changeStatus ( PlayerStatus.PLAYER_PREPARED );
         }
      @Override
      public void OnCompletionWithParam ( int param )
         {
         }
      private void changeStatus ( PlayerStatus status )
         {
            mPlayerStatus = status;
            if ( this.mediaController != null )
               {
                  this.mediaController.changeStatus ( status );
               }
         }

      public void onClickEmptyArea ( View v )
         {
            if ( barTimer != null )
               {
                  barTimer.cancel ( );
                  barTimer = null;
               }
            if ( this.mediaController != null )
               {
                  if ( mediaController.getVisibility ( ) == View.VISIBLE )
                     {
                        mediaController.hide ( );
                     }
                  else
                     {
                        mediaController.show ( );
                        hideOuterAfterFiveSeconds ( );
                     }
               }
         }

      private void hideOuterAfterFiveSeconds ( )
         {
            if ( barTimer != null )
               {
                  barTimer.cancel ( );
                  barTimer = null;
               }
            barTimer = new Timer ( );
            barTimer.schedule ( new TimerTask ( ) {

                     @Override
                     public void run ( )
                        {
                           if ( mediaController != null )
                              {
                                 mediaController.getMainThreadHandler ( ).post ( new Runnable ( ) {

                                          @Override
                                          public void run ( )
                                             {
                                                mediaController.hide ( );
                                             }

                                       } );
                              }
                        }

                  }, 5 * 1000 );

         }
      public boolean onKeyDown ( int keyCode, KeyEvent event )
         {
            if ( keyCode == KeyEvent.KEYCODE_BACK )
               {
                  if ( ( System.currentTimeMillis ( ) - mExitTime ) > 2000 )
                     {
                        Object mHelperUtils;
                        Toast.makeText ( this, "再按一次退出播放", Toast.LENGTH_SHORT ).show ( );
                        mExitTime = System.currentTimeMillis ( );

                     }
                  else
                     {
                        finish ( );
                     }
                  return true;
               }
            return super.onKeyDown ( keyCode, event );
         }
   }



