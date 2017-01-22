package com.perfume.VideoPlay;

import android.os.*;
import android.view.*;
import android.widget.*;
import com.baidu.cyberplayer.core.BVideoView.*;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.baidu.cyberplayer.core.BVideoView;
import com.perfume.R;
/*
播放器 控制条处理
*/
public class MediaVideo extends RelativeLayout implements OnPositionUpdateListener, OnTotalCacheUpdateListener {
      private boolean isPrepared = false;
      private ImageButton playButton;
      private TextView positionView;
      private SeekBar seekBar;
      private TextView durationView;
      private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
      public Handler getMainThreadHandler() {
            return mainThreadHandler;
         }
      private BVideoView mVideoView = null;
      boolean mbIsDragging = false;
      public MediaVideo(Context context, AttributeSet attrs) {
            super(context, attrs);
            initUI();
         }
      public MediaVideo(Context context) {
            super(context);
            initUI();
         }
      private void initUI() {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.bar_simple_media_controller, this);
            playButton = (ImageButton) layout.findViewById(R.id.btn_play);
            playButton.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View v) {
                           if (mVideoView == null) {
                              } else {
                                 if (!isPrepared) {
                                       mVideoView.start();
                                       changeStatus(Play.PlayerStatus.PLAYER_PREPARING);
                                    } else {
                                       if (mVideoView.isPlaying()) {
                                             playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                                             mVideoView.pause();
                                          } else {
                                             playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                                             mVideoView.resume();
                                          }
                                    }
                              }
                        }

                  });

            positionView = (TextView) layout.findViewById(R.id.tv_position);
            seekBar = (SeekBar) layout.findViewById(R.id.seekbar);
            seekBar.setMax(0);
            durationView = (TextView) layout.findViewById(R.id.tv_duration);

            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                     public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                           updatePostion(progress);
                        }

                     public void onStartTrackingTouch(SeekBar seekBar) {
                           mbIsDragging = true;
                        }

                     public void onStopTrackingTouch(SeekBar seekBar) {
                           if (mVideoView.getDuration() > 0) {
                                 currentPositionInSeconds = seekBar.getProgress();
                                 if (mVideoView != null) {
                                       mVideoView.seekTo(seekBar.getProgress());
                                    }

                                 if (currentStatus == Play.PlayerStatus.PLAYER_COMPLETED) {
                                       mVideoView.start();
                                       changeStatus(Play.PlayerStatus.PLAYER_PREPARING);
                                    }
                              }
                           mbIsDragging = false;
                        }
                  });
            enableControllerBar(false);
         }

      private Play.PlayerStatus currentStatus = Play.PlayerStatus.PLAYER_IDLE;

      public void changeStatus(final Play.PlayerStatus status) {
            currentStatus = status;
            isMaxSetted = false;
            mainThreadHandler.post(new Runnable() {

                     @Override
                     public void run() {
                           if (status == Play.PlayerStatus.PLAYER_IDLE) {
                                 playButton.setEnabled(true);
                                 playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                                 seekBar.setEnabled(false);
                                 updatePostion(mVideoView == null ? 0 : mVideoView.getCurrentPosition());
                                 updateDuration(mVideoView == null ? 0 : mVideoView.getDuration());
                                 isPrepared = false;
                              } else if (status == Play.PlayerStatus.PLAYER_PREPARING) {
                                 playButton.setEnabled(false);
                                 playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                                 seekBar.setEnabled(false);
                                 isPrepared = false;
                              } else if (status == Play.PlayerStatus.PLAYER_PREPARED) {
                                 isPrepared = true;
                                 playButton.setEnabled(true);
                                 playButton.setBackgroundResource(R.drawable.toggle_btn_pause);
                                 seekBar.setEnabled(true);
                              } else if (status == Play.PlayerStatus.PLAYER_COMPLETED) {
                                 playButton.setEnabled(true);
                                 playButton.setBackgroundResource(R.drawable.toggle_btn_play);
                                 isPrepared = false;
                              }
                        }

                  });

         }

      public void setMediaPlayerControl(BVideoView player) {
            mVideoView = player;
            mVideoView.setOnPositionUpdateListener(this);
            mVideoView.setOnTotalCacheUpdateListener(this);
         }

      public void show() {
            if (mVideoView == null) {
                  return;
               }

            if (mVideoView.getDuration() > 0) {
                  setProgress((int) currentPositionInSeconds);
               }

            this.setVisibility(View.VISIBLE);
         }

      public void hide() {
            this.setVisibility(View.GONE);
         }

      public boolean getIsDragging() {
            return mbIsDragging;
         }

      private void updateDuration(int second) {
            if (durationView != null) {
                  durationView.setText(formatSecond(second));
               }
         }

      private void updatePostion(int second) {
            if (positionView != null) {
                  positionView.setText(formatSecond(second));
               }
         }

      private String formatSecond(int second) {
            int hh = second / 3600;
            int mm = second % 3600 / 60;
            int ss = second % 60;
            String strTemp = null;
            if (0 != hh) {
                  strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
               } else {
                  strTemp = String.format("%02d:%02d", mm, ss);
               }
            return strTemp;
         }

      private boolean isMaxSetted = false;

      public void setMax(int maxProgress) {
            if (isMaxSetted) {
                  return;
               }
            if (seekBar != null) {
                  seekBar.setMax(maxProgress);
               }
            updateDuration(maxProgress);
            if (maxProgress > 0) {
                  isMaxSetted = true;
               }
         }

      public void setProgress(int progress) {
            if (seekBar != null) {
                  seekBar.setProgress(progress);
               }
         }

      public void setCache(int cache) {
            if (seekBar != null && cache != seekBar.getSecondaryProgress()) {
                  seekBar.setSecondaryProgress(cache);
               }
         }

      public void enableControllerBar(boolean isEnable) {
            seekBar.setEnabled(isEnable);
            playButton.setEnabled(isEnable);
         }

      private long currentPositionInSeconds = 0L;

      @Override
      public boolean onPositionUpdate(long newPositionInMilliSeconds) {
            long newPositionInSeconds = newPositionInMilliSeconds / 1000;
            long previousPosition = currentPositionInSeconds;
            if (newPositionInSeconds > 0 && !getIsDragging()) {
                  currentPositionInSeconds = newPositionInSeconds;
               }
            if (getVisibility() != View.VISIBLE) {
                  // 如果控制条不可见，则不设置进度
                  return false;
               }
            if (!getIsDragging()) {
                  int duration = mVideoView.getDuration();
                  if (duration > 0) {
                        this.setMax(duration);
                        // 直播视频的duration为0，此时不设置进度
                        if (newPositionInSeconds > 0 && previousPosition != newPositionInSeconds) {
                              this.setProgress((int) newPositionInSeconds);
                           }
                     }
               }
            return false;
         }

      @Override
      public void onTotalCacheUpdate(final long arg0) {
            mainThreadHandler.post(new Runnable() {

                     @Override
                     public void run() {
                           setCache((int) arg0 + 10);
                        }

                  });
         }

   }

