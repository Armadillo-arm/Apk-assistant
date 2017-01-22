package com.perfume.Utis;
import android.content.Context;
import android.widget.Toast;
/*
TOAST类
*/
public class ToastShow
   {
      public static void LongShow ( Context thzz, String msg )
         {
            Toast.makeText(thzz,msg,Toast.LENGTH_LONG).show();
         }
      public static void ShortShow ( Context thzz, String msg )
         {
            Toast.makeText(thzz,msg,Toast.LENGTH_SHORT).show();
         }
   }
