package com.perfume.Plugin;
import android.content.*;
import java.io.*;
import java.util.*;

import android.support.v7.app.AlertDialog;
import com.maxleap.al;
import com.perfume.R;
import com.perfume.activity.ApktoolProject;
import dalvik.system.DexClassLoader;
import com.perfume.Utis.ShowDialog;

public class PluginManager
   {
      String PluginPath="/sdcard/ApktoolHelper/Plugin";
      SharedPreferences shared;
      Context context;
      PluginInfo info;
      ApktoolProject open;
      public void setOpenApktool ( ApktoolProject open )
         {
            this.open = open;
         }
      public PluginManager ( Context context )
         {
            shared = context.getSharedPreferences ( "Plugin", context.MODE_PRIVATE );
            this.context = context;
            if ( !getPluginPath ( ).exists ( ) )
               getPluginPath ( ).mkdirs ( );
         }
      public void showLocalPlugin ( final File path )
         {
            AlertDialog.Builder al=new AlertDialog.Builder ( context );
            final File[] zips=getPluginPath ( ).listFiles ( new FileFilter ( ){
                     @Override
                     public boolean accept ( File p1 )
                        {
                           if ( p1.getName ( ).endsWith ( ".zip" ) )
                              return true;
                           else
                              return false;
                        }
                  } );
            if ( zips.length == 0 )
               {
                  ShowDialog.showErrorBox(open,"No Plugin");
               }
            String[] fs=new String[zips.length];
            for ( int y=0;y < zips.length;y += 1 )
               {
                  fs [ y ] = zips [ y ].getName ( );
               }
            al.setNegativeButton ( android.R.string.cancel, null );
            al.setItems ( fs, new DialogInterface.OnClickListener ( ){
                     @Override
                     public void onClick ( DialogInterface p1, int p2 )
                        {
                           ShowPluginItem ( zips [ p2 ], path );
                        }
                  } );
            al.show ( );
         }
      public void ShowPluginItem ( final File path, final File asFile )
         {
            AlertDialog.Builder a;
            try
               {
                  info = new PluginInfo ( path );
               }
            catch (IOException e)
               {
               }
            a = new AlertDialog.Builder ( context );
            a.setMessage ( info.toString ( ) );
            a.setPositiveButton ("Run" , new DialogInterface.OnClickListener ( ){
                     @Override
                     public void onClick ( DialogInterface p1, int p2 )
                        {
                           startPlugin ( path.getAbsolutePath ( ), context.getClassLoader ( ), asFile, info );
                        }
                  } );
         }
      public void startPlugin ( String path, ClassLoader loader, File filepath, PluginInfo info )
         {
            File cache=new File ( context.getFilesDir ( ), "DexCache" );
            if ( !cache.exists ( ) )cache.mkdirs ( );
            DexClassLoader dexload=new DexClassLoader ( path, cache.getAbsolutePath ( ), null, loader );
            try
               {
                  Class<PluginHelper> clazz=(Class<PluginHelper>) dexload.loadClass ( info.getClassName ( ) );
                  try
                     {
                        PluginHelper plugin = clazz.newInstance ( );
                        plugin.setInfo ( info );
                        if ( open != null )
                           plugin.setApktoolProject ( open );
                        if ( filepath == null )
                           {
                              if ( open != null )
                                 plugin.OpenProject ( open );
                              else
                                 plugin.OpenInfo();
                           }
                        else
                           plugin.OpenFile ( filepath );
                     }
                  catch (InstantiationException e)
                     {
                     }
                  catch (IllegalAccessException e)
                     {
                     }
               }
            catch (ClassNotFoundException e)
               {
               }
         }
      public  void putPlugin ( String s )
         {
            Set<String>  sets = new HashSet<String> ( );
            sets = shared.getStringSet ( "file_plugin", sets );
            sets.add ( s );
            shared.edit ( ).putStringSet ( "file_plugin", sets ).commit ( );
         }
      public List<String> getInstallPlugin ( )
         {
            Set<String>  sets = new HashSet<String> ( );
            sets = shared.getStringSet ( "file_plugin", sets );
            List<String> installed=new ArrayList <String> ( );
            Iterator i=sets.iterator ( );
            while ( i.hasNext ( ) )
               installed.add ( (String)i.next ( ) );
            return installed;
         }
      public File getPluginPath ( )
         {
            File path = new File ( PluginPath );
            if ( !path.exists ( ) )path.mkdirs ( );
            return path;
         }
   }

