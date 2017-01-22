/*
 * This is the source code of Telegram for Android v. 1.7.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.perfume.FileAdapter;

import android.content.pm.*;
import android.graphics.*;
import android.widget.*;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import com.bumptech.glide.Glide;
import com.perfume.App;
import com.perfume.Utis.ApkInfo;
import com.perfume.R;
/*
画图
*/
public class TextDetailDocumentsCell extends FrameLayout
   {
      public static Context c;
      private TextView textView;
      private TextView valueTextView;
      private TextView typeTextView;
      private ImageView imageView;
      private CheckBox checkBox;

      public TextDetailDocumentsCell ( Context context )
         {
            super ( context );

            textView = new TextView ( context );
            textView.setTextColor ( 0xff000000 );
            textView.setTextSize ( TypedValue.COMPLEX_UNIT_DIP, 16 );
            textView.setLines ( 1 );
            textView.setMaxLines ( 1 );
            textView.setSingleLine ( true );
            textView.setGravity ( Gravity.LEFT );
            addView ( textView );
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams ( );
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.topMargin = AndroidUtilities.dp ( 10 );
            layoutParams.leftMargin = AndroidUtilities.dp ( 71 );
            layoutParams.rightMargin = AndroidUtilities.dp ( 16 );
            layoutParams.gravity = Gravity.LEFT;
            textView.setLayoutParams ( layoutParams );

            valueTextView = new TextView ( context );
            valueTextView.setTextColor ( 0xff000000 );
            valueTextView.setTextSize ( TypedValue.COMPLEX_UNIT_DIP, 13 );
            valueTextView.setLines ( 1 );
            valueTextView.setMaxLines ( 1 );
            valueTextView.setSingleLine ( true );
            valueTextView.setGravity ( Gravity.LEFT );
            addView ( valueTextView );
            layoutParams = (LayoutParams) valueTextView.getLayoutParams ( );
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.topMargin = AndroidUtilities.dp ( 35 );
            layoutParams.leftMargin = AndroidUtilities.dp ( 71 );
            layoutParams.rightMargin = AndroidUtilities.dp ( 16 );
            layoutParams.gravity = Gravity.LEFT;
            valueTextView.setLayoutParams ( layoutParams );

            typeTextView = new TextView ( context );
            typeTextView.setBackgroundColor ( 0xff000000 );
            typeTextView.setEllipsize ( TextUtils.TruncateAt.MARQUEE );
            typeTextView.setGravity ( Gravity.CENTER );
            typeTextView.setSingleLine ( true );
            typeTextView.setTextColor ( 0xffffffff );
            typeTextView.setTextSize ( TypedValue.COMPLEX_UNIT_DIP, 16 );
            typeTextView.setTypeface ( Typeface.DEFAULT_BOLD );
            addView ( typeTextView );
            layoutParams = (LayoutParams) typeTextView.getLayoutParams ( );
            layoutParams.width = AndroidUtilities.dp ( 40 );
            layoutParams.height = AndroidUtilities.dp ( 40 );
            layoutParams.leftMargin = AndroidUtilities.dp ( 16 );
            layoutParams.rightMargin = AndroidUtilities.dp ( 0 );
            layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            typeTextView.setLayoutParams ( layoutParams );

            imageView = new ImageView ( context );
            addView ( imageView );
            layoutParams = (LayoutParams) imageView.getLayoutParams ( );
            layoutParams.width = AndroidUtilities.dp ( 40 );
            layoutParams.height = AndroidUtilities.dp ( 40 );
            layoutParams.leftMargin = AndroidUtilities.dp ( 16 );
            layoutParams.rightMargin = AndroidUtilities.dp ( 0 );
            layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams ( layoutParams );

            checkBox = new CheckBox ( context );
            checkBox.setVisibility ( GONE );
            addView ( checkBox );
            layoutParams = (LayoutParams) checkBox.getLayoutParams ( );
            layoutParams.width = AndroidUtilities.dp ( 22 );
            layoutParams.height = AndroidUtilities.dp ( 22 );
            layoutParams.topMargin = AndroidUtilities.dp ( 34 );
            layoutParams.leftMargin = AndroidUtilities.dp ( 38 ) ;
            layoutParams.rightMargin = 0;
            layoutParams.gravity = Gravity.LEFT;
            checkBox.setLayoutParams ( layoutParams );
         }

      @Override
      protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec )
         {
            super.onMeasure ( widthMeasureSpec, MeasureSpec.makeMeasureSpec ( AndroidUtilities.dp ( 64 ), MeasureSpec.EXACTLY ) );
         }

      public void setTextAndValueAndTypeAndThumb ( String text, String value, String type, String thumb, int resId )
         {
            textView.setText ( text );
            valueTextView.setText ( value );
            if ( type != null )
               {
                  typeTextView.setVisibility ( VISIBLE );
                  typeTextView.setText ( type );
               }
            else
               {
                  typeTextView.setVisibility ( GONE );
               }
            if ( thumb != null || resId != 0 )
               {
                  imageView.setVisibility ( VISIBLE );
                  if ( thumb != null || resId == 100 || resId == 200)
                     {
                        if(resId==100)
                        {
                           imageView.setImageDrawable(getAppIcon(thumb));
                        }else
                        {
                           Glide.with ( App.thzz )
                              .load ( thumb )
                              .centerCrop ( )
                              .crossFade ( )
                              .into ( imageView );
                        }
                     }
                  else
                     {
                        imageView.setImageResource ( resId );
                     }
                  
               }
            else
               {
                  imageView.setVisibility ( GONE );
               }
         }
      public void setChecked ( boolean checked, boolean animated )
         {
            if ( checkBox.getVisibility ( ) != VISIBLE )
               {
                  checkBox.setVisibility ( VISIBLE );
               }
            checkBox.setChecked ( checked );
         }
      public  Drawable getAppIcon ( String path )
         {
            if ( path == null )return null;
            PackageManager pm=c.getPackageManager ( );
            PackageInfo info=pm.getPackageArchiveInfo ( path, pm.GET_ACTIVITIES );
            if ( info == null )
               return null;
            ApplicationInfo appinfo=info.applicationInfo;
            appinfo.sourceDir = path;
            appinfo.publicSourceDir = path;
            return appinfo.loadIcon ( pm );

         }
   }
