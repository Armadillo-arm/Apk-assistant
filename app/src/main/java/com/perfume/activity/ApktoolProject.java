package com.perfume.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.perfume.R;
import com.perfume.Utis.AssetsFile;
import java.io.File;
import com.perfume.Utis.AndroidManifestRead;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
/*
APKTOOL工程
*/
public class ApktoolProject extends AppCompatActivity
   {
      public static int ProjectType;
      public static String ProjectPath;
      @Override
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            setContentView(R.layout.projectmain);
            ProjectPath=getIntent().getStringExtra("Path");
          //  ProjectType=GetType();
            //if(ProjectType==1||ProjectType==2)
           // {
               try
                  {
                     AndroidManifestRead xml=new AndroidManifestRead ( new File ( ProjectPath + "/AndroidManifest.xml" ) );
                     getSupportActionBar ( ).setTitle ( "APK Project");
                     getSupportActionBar().setSubtitle(xml.getPackage());
                  }
               catch (IOException e)
                  {}
               catch (ParserConfigurationException e)
                  {}
               catch (SAXException e)
                  {}
            //}
         }
         private int GetType()
         {
            int i=Integer.parseInt( AssetsFile.txt2String(new File(ProjectPath+"/ProjectType.txt")));
            return i;
         }
}
