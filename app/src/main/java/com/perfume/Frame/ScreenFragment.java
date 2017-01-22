package com.perfume.Frame;
import android.media.projection.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import com.perfume.R;
import com.perfume.UI.ScreenRecorder;
import java.io.File;

public class ScreenFragment extends Fragment
   {
      private View mView;
      private Button Start;
      private static final int REQUEST_CODE = 1;
      private MediaProjectionManager mMediaProjectionManager;
      private ScreenRecorder mRecorder;
      @Override
      public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
         {
            if ( mView == null )
               {
                  mView = inflater.inflate ( R.layout.activity_pm, container, false );
                  inti ( );
                  mMediaProjectionManager = (MediaProjectionManager)getActivity(). getSystemService(getActivity(). MEDIA_PROJECTION_SERVICE);
               }
            return mView;
         }
      private void inti ( )
         {
            Start = (Button) mView.findViewById ( R.id.button );
            Start.setOnClickListener ( new OnClickListener ( ){

                     @Override
                     public void onClick ( View p1 )
                        {
                           if (mRecorder != null) {
                                 mRecorder.quit();
                                 mRecorder = null;
                                 Start.setText("Restart recorder");
                              } else {
                                 Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                                 startActivityForResult(captureIntent, REQUEST_CODE);
                              }
                        }
                  } );
         }

      @Override
      public void onActivityResult ( int requestCode, int resultCode, Intent data )
         {
           
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                //  Log.e("@@", "media projection is null");
                  return;
               }
            // video size
            final int width = 1280;
            final int height = 720;
            File file = new File(Environment.getExternalStorageDirectory(),
                                 "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");
            final int bitrate = 6000000;
            mRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
            mRecorder.start();
            Start.setText("Stop Recorder");
            Toast.makeText(getActivity(), "Screen recorder is running...", Toast.LENGTH_SHORT).show();
          //  moveTaskToBack(true);
         }
         
   }
