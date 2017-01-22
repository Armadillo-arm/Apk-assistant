package com.perfume.Utis;

import android.content.*;
import java.util.*;

import android.content.pm.PackageInfo;
import android.net.Uri;
/*
QQ互联
*/
public class Tencent
{
   public static boolean StartQ(Context thzz,String key)
   {
      Intent intent = new Intent();
      intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
      try {
            thzz.startActivity(intent);
            return true;
         } catch (Exception e) {
            return false;
         }
   }
   public static boolean IsInstallQ(Context thzz,String Pack)
   {

      List installedPackages = thzz.getPackageManager ( ).getInstalledPackages ( 0 );
      List arrayList = new ArrayList( );
      if ( installedPackages != null )
         {
            for ( int i = 0; i < installedPackages.size ( ); i++ )
               {
                  arrayList.add ( ( (PackageInfo) installedPackages.get ( i ) ).packageName );
               }
         }
      return arrayList.contains ( Pack );
   }
   public static void StartQQ(Context thzz)
   {
      thzz.startActivity ( new Intent( "android.intent.action.VIEW", Uri.parse ( String.format ( "mqqwpa://im/chat?chat_type=wpa&uin=%s&version=1&src_type=web&web_src=null", new Object[]{"1834661238"} ) ) ) );
   }
   
}
