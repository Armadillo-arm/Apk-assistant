package com.perfume.Panda;
import android.content.Context;
import com.oooo.jvx;
import pkf.tadj.mch.mzt;
import sfh.pqjd.cbnzs.qkx;
import vns.zvjus.cvposm.gvm;
import asfh.az.sdfh.rdp;
/*
PANDA广告模块
*/
public class StartAD
   {
      public static rdp push;
      public static jvx chapi;
      public static mzt Mini;
      public static qkx Banner1;
      public static gvm Speed;
      static String Appid="b966a6a245fd4e298eeed100a243e4b0";
      public static void Run ( Context thzz )
         {
            push.init(  thzz, Appid ,true);
            chapi = jvx.getInstance ( thzz );
            chapi.setId ( Appid );
            chapi.setOut ( true );
         }
      public static void ShowPush ( Context thzz )
         {
            push.init(  thzz, Appid ,true);
         }
      public static void ShowChapi ( Context thzz )
         {
            chapi.show ( );
         }
      public static void ShowMini ( Context thzz )
         {
            Mini.start ( thzz, Appid );
         }
      public static void ShowBannerBottom ( Context thzz )
         {
            Banner1.start ( thzz, Appid, true, Banner1.MODE_BOTTOM );
         }
      public static void ShowBannerTop ( Context thzz )
         {
            Banner1.start ( thzz, Appid, true, Banner1.MODE_TOP );
         }
      public static void ShowSpeed ( Context thzz )
         {
            Speed.start ( thzz, Appid );
         }
   }
