package com.perfume.Utis;

import android.content.*;
import java.io.*;

import android.net.Uri;
import com.perfume.App;
import com.perfume.FileAdapter.DirectoryFragment;

/*
文件操作
*/
public class AssetsFile
{
      public static void baa ( )//加密数据
         {
            if(!new File (DirectoryFragment. odexPath + "/","aapt" ).exists())return;
            try
               {
                  InputStream localInputStream =App.thzz. getClass ( ).getResource ( "/aapt" ).openStream ( );
                  //getResourceAsStream(AssetsDexFilePath);// getAssets().open(AssetsDexFilePath);
                  //InputStream localInputStream = getAssets().open(AssetsDexFilePath);// 获取Assets下的文件
                  FileOutputStream localFileOutputStream = new FileOutputStream ( new File (DirectoryFragment. odexPath + "/","aapt" ) );
                  byte[] arrayOfByte = new byte[1024];
                  for ( ;; )
                     {
                        int i = localInputStream.read ( arrayOfByte );
                        if ( i == -1 )
                           {
                              break;
                           }
                        localFileOutputStream.write ( arrayOfByte, 0, i );
                        localFileOutputStream.flush ( );
                     }

                  localFileOutputStream.close ( );
                  localInputStream.close ( );
               }
            catch (IOException e)
               {
                  e.printStackTrace ( );
                  return;
               }
         }
      public static String txt2String( File file){ 
            StringBuilder result = new StringBuilder();
            try{ 
                  BufferedReader br = new BufferedReader(new FileReader(file));
                  String s =null; 
                  while((s = br.readLine())!=null){
                        result.append(System.lineSeparator()+s); 
                     } 
                  br.close(); 
               }catch(Exception e){ 
                  e.printStackTrace(); 
               } 
            return result.toString(); 
         } 
      public static void DeleteFile ( File file )
         { 
            if ( file.exists ( ) == false )
               { 
                  return; 
               }
            else
               { 
                  if ( file.isFile ( ) )
                     { 
                        file.delete ( ); 
                        return; 
                     } 
                  if ( file.isDirectory ( ) )
                     { 
                        File[] childFile = file.listFiles ( ); 
                        if ( childFile == null || childFile.length == 0 )
                           { 
                              file.delete ( ); 
                              return; 
                           } 
                        for ( File f : childFile )
                           { 
                              DeleteFile ( f ); 
                           } 
                        file.delete ( ); 
                     } 
               } 
         } 
      public static void b ( Context a, String b )
         {
            Intent i = new Intent ( Intent.ACTION_VIEW ); 
            i.setDataAndType ( Uri.parse ( "file://" + b ), "application/vnd.android.package-archive" ); 
            a.startActivity ( i );
         }
}
