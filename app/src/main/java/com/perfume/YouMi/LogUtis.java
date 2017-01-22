package com.perfume.YouMi;
import java.text.Format;
/*
有米广告log
*/
public class LogUtis
   {
      public static void Log ( String i )
         {
            System.out.println ( i );
         }
      public static void Log ( String i, Object... d )
         {
            System.out.println ( String.format ( i, d ) );
         }
   }
