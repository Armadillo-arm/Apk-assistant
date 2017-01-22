package com.perfume.activity;
import android.support.design.widget.*;
import android.view.*;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.perfume.R;
/*
关于界面
*/
public class ActivityAbout extends AppCompatActivity
   {
      private FloatingActionButton fab;
      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(this.getString(R.string.app_name));
            fab= (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                           /**
                            * 分享处理
                            */
                           
                        }
                  });
         }
      @Override
      public boolean onOptionsItemSelected(MenuItem item)
         {
            switch (item.getItemId())
               {
                  case android.R.id.home:
                     onBackPressed();
                     return true;
               }
            return super.onOptionsItemSelected(item);
         }
   }
