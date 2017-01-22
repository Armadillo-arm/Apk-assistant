package com.perfume.Utis;

import android.view.View;
import android.support.design.widget.Snackbar;
import android.view.View.OnClickListener;

/*
Snack提示类
*/
public class SnackbarShow
   {
      
      private   OnTextClick mOnTextClick= null;
      public void setOnItemClickListener ( OnTextClick listener )
         {
            mOnTextClick = listener;
         }
      public static interface  OnTextClick
         {
            void OnSnackbarClick ( View v );
         }
      public  void LongShow ( View v, String msg ,String act)
         {
            Snackbar.make ( v, msg, Snackbar.LENGTH_LONG ).setAction ( act, new OnClickListener ( ){

                     @Override
                     public void onClick ( View p1 )
                        {
                           mOnTextClick.OnSnackbarClick ( p1 );
                        }
                  } ).show ( );
         }
      public  void ShortShow ( View v, String msg,String act )
         {
            Snackbar.make ( v, msg, Snackbar.LENGTH_SHORT ).setAction ( act, new OnClickListener ( ){

                     @Override
                     public void onClick ( View p1 )
                        {
                           mOnTextClick.OnSnackbarClick ( p1 );
                        }
                  } ).show ( );
         }
   }
