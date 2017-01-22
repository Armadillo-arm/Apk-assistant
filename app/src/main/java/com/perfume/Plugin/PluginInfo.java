package com.perfume.Plugin;

import java.io.*;
import java.util.zip.*;

import java.util.HashMap;

public class PluginInfo
   {
      ZipFile zip;
      HashMap<String,String> items;
      public  PluginInfo ( File path ) throws IOException
         {
            zip = new ZipFile ( path );
            InputStream is=zip.getInputStream ( new ZipEntry ( "配置文件.txt" ) );
            byte[] b=new byte[is.available ( )];
            is.read ( b );
            String data=new String ( b );
            String[] attrs=data.split ( "\n" );
            items = new HashMap<String,String> ( );
            for ( String it:attrs )
               {
                  String[] vs=it.split ( "," );
                  if ( vs.length != 2 )continue;
                  items.put ( vs [ 0 ], vs [ 1 ] );
               }
         }

      public String getName ( )
         {
            return items.get ( "name" );
         }
      public String getInfo ( )
         {
            return items.get ( "info" );
         }
      public String getVersion ( )
         {
            return items.get ( "version" );
         }
      public String getAuthor ( )
         {
            return items.get ( "author" );
         }
      public String getClassName ( )
         {
            return items.get ( "class" );
         }

      public InputStream getStream ( String name ) throws IOException
         {
            return zip.getInputStream ( new ZipEntry ( name ) );
         }

      @Override
      public String toString ( )
         {
            return "作者:" + getAuthor ( ) + "\n版本:" + getVersion ( ) + "\n介绍:" + getInfo ( );
         }


   }

