package com.perfume.Plugin;

import java.io.*;

import android.content.Context;
import com.perfume.App;
import com.perfume.activity.ApktoolProject;

public abstract class PluginHelper
   {
    public PluginInfo info;
      public ApktoolProject open;
      public ApktoolProject getOpenApktoolProject()
         {
            return open;
         }
      public void setApktoolProject(ApktoolProject open)
         {
            this. open=open;
         }
      public void setInfo(PluginInfo info)
         {this.info = info;}
      public Context getContext()
         {
            return App.thzz;
         }
      public  InputStream getResourse ( String name ) throws IOException
         {
            return info.getStream ( name );
         }
      public abstract void OpenInfo ( );

      public abstract void OpenFile ( File file );

      public abstract void OpenProject ( ApktoolProject open );
   }

