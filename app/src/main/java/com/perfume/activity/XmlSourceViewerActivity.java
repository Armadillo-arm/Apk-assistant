/*
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.perfume.activity;

import android.os.*;
import android.webkit.*;
import java.io.*;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import com.perfume.R;
import com.perfume.Utis.AssetsFile;

/**
 * Simple example that parses the AndroidManifest.xml and displays the source in a WebView
 */
 /*
 文本预览 xml预览
 */
public class XmlSourceViewerActivity extends AppCompatActivity
   {

      private WebView webView;
      private String app;
      private String sourceCodeText;
      public static String buff;
      public static Boolean isfile=true;

      private final WebViewClient webViewClient = new WebViewClient ( ) {

            @Override public void onPageFinished ( WebView view, String url )
               {
                  ProgressBar progress = (ProgressBar) findViewById ( R.id.progress );
                  progress.setVisibility ( View.GONE );
               }

            @Override public WebResourceResponse shouldInterceptRequest ( WebView view, String url )
               {
                  InputStream stream = inputStreamForAndroidResource ( url );
                  if ( stream != null )
                     {
                        return new WebResourceResponse ( "text/javascript", "UTF-8", stream );
                     }
                  return super.shouldInterceptRequest ( view, url );
               }

            private InputStream inputStreamForAndroidResource ( String url )
               {
                  final String ANDROID_ASSET = "file:///android_asset/";

                  if ( url.contains ( ANDROID_ASSET ) )
                     {
                        url = url.replaceFirst ( ANDROID_ASSET, "" );
                        try
                           {
                              AssetManager assets = getAssets ( );
                              Uri uri = Uri.parse ( url );
                              return assets.open ( uri.getPath ( ), AssetManager.ACCESS_STREAMING );
                           }
                        catch (IOException e)
                           {
                              e.printStackTrace ( );
                           }
                     }
                  return null;
               }

         };

      @Override 
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            setContentView ( R.layout.source_viewer );
            app = getIntent ( ).getStringExtra ( "Path" );
            //  xml = getIntent().getStringExtra("xml");
            getSupportActionBar ( ).setTitle ( app );

            webView = (WebView) findViewById ( R.id.source_view );
            webView.getSettings ( ).setJavaScriptEnabled ( true );
            webView.getSettings ( ).setDefaultTextEncodingName ( "UTF-8" );
            webView.setWebViewClient ( webViewClient );

            if ( savedInstanceState != null && savedInstanceState.containsKey ( "source" ) )
               {
                  sourceCodeText = savedInstanceState.getString ( "source" );
               }
            if ( sourceCodeText == null )
               {
                  if(isfile)
                  {
                     new AndroidXmlLoader ( ).executeOnExecutor ( AsyncTask.THREAD_POOL_EXECUTOR, new File ( app ) );
                  }
                  else
                  {
                     new ReadString ( ).executeOnExecutor ( AsyncTask.THREAD_POOL_EXECUTOR,buff );
                  }
               }
            else
               {
                  loadSourceCode ( sourceCodeText );
               }
         }
      @Override protected void onSaveInstanceState ( Bundle outState )
         {
            super.onSaveInstanceState ( outState );
            outState.putString ( "source", sourceCodeText );
         }
      private void loadSourceCode ( String html )
         {
            String data = String.format (
               "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3" +
               ".org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3" +
               ".org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
               "charset=utf-8\" /><script src=\"run_prettify.js?skin=github\"></script></head><body " +
               "bgcolor=\"white\"><pre class=\"prettyprint linenums\">%s</pre></body></html>",
               html );
            webView.loadDataWithBaseURL ( "file:///android_asset/", data, "text/html", "UTF-8", null );
         }

      private final class AndroidXmlLoader extends AsyncTask<File, Void, String>
         {

            @Override 
            protected String doInBackground ( File... params )
               {
                  String   source=AssetsFile.txt2String ( params [ 0 ] );
                  return Html.escapeHtml ( source );
               }

            @Override protected void onPostExecute ( String escapedHtml )
               {
                  sourceCodeText = escapedHtml;
                  loadSourceCode ( escapedHtml );
               }
         }
      private final class ReadString extends AsyncTask<String, Void, String>
         {

            @Override 
            protected String doInBackground ( String... params )
               {
                  return Html.escapeHtml ( params[0] );
               }

            @Override protected void onPostExecute ( String escapedHtml )
               {
                  sourceCodeText = escapedHtml;
                  loadSourceCode ( escapedHtml );
               }
         }
   }
