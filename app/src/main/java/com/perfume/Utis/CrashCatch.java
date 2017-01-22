package com.perfume.Utis;


import android.content.*;
import java.io.*;

import java.lang.Thread.UncaughtExceptionHandler;

/*
全局异常捕获
*/
public class CrashCatch implements UncaughtExceptionHandler
   {

      private static CrashCatch instance;
      Context con;
      public static CrashCatch getInstance ( )
         {
            if ( instance == null )
               {
                  instance = new CrashCatch ( );
               }
            return instance;
         }

      public void init ( Context ctx )
         {
            con = ctx;
            Thread.setDefaultUncaughtExceptionHandler ( this );
         }

      /**
       * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
       */
      @Override
      public void uncaughtException ( Thread arg0, Throwable arg1 )
         {
            if ( arg1 == null )return;
            StackTraceElement[] stackTrace = arg1.getStackTrace ( );
            StringBuilder sb=new StringBuilder ( );
            sb.append ( "error:" + arg1.toString ( ) );
            sb.append ( "\n" );
            for ( int i = 0; i < stackTrace.length; i++ )
               {  
                  sb.append ( stackTrace [ i ].toString ( ) );
                  sb.append ( "\n" );
               }
            stringtosd ( sb.toString ( ), "/sdcard/Apk助手崩溃日志.txt" );
            
            arg1.printStackTrace ( );
            android.os.Process.killProcess ( android.os.Process.myPid ( ) );
         }
     public static Boolean stringtosd(String str,String path)
         {
            File f=new File(path);
            if(!f.getParentFile().exists())
               f.getParentFile().mkdirs();
            try
               {
                  f.createNewFile();
               }
            catch (IOException e)
               {
                  return false;
               }
            OutputStream os=null;
            try
               {
                  os = new FileOutputStream(f);
               }
            catch (FileNotFoundException e)
               {
                  return false;
               }

            try
               {
                  os.write(str.getBytes());
               }
            catch (IOException e)
               {
                  return false;
               }

            try
               {
                  os.close();
               }
            catch (IOException e)
               {

                  return false;
               }
            return true;
         }
   }


