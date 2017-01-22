package com.perfume.activity;
import com.perfume.*;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
/*
图片查看
*/
public class ImageViewer extends AppCompatActivity
   {
      public static String Path="";
      public static byte[] ImageBuff;
      private ImageView mimage;
      @Override
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            setContentView(R.layout.activitty_image);
            getSupportActionBar().setTitle("Image View");
            if(Path.isEmpty())
            {
               getSupportActionBar().setSubtitle("Zip View");
               mimage=(ImageView)findViewById(R.id.src);
               Glide.with(App.thzz).load(ImageBuff).crossFade().into(mimage);
               return;
            }
            getSupportActionBar().setSubtitle(Path);
            mimage=(ImageView)findViewById(R.id.src);
            Glide.with(App.thzz).load(Path).crossFade().into(mimage);
         }
}
