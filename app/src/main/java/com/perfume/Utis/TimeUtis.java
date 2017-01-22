package com.perfume.Utis;

import java.net.*;

import android.os.AsyncTask;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
/*
时间操作
*/
public class TimeUtis
{
      public  static long getCurTimeMills() {
            MyTask myTask=new MyTask();
            try {
                  Date d= myTask.execute("http://www.360.cn").get();
                  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  return d.getTime();
               } catch (InterruptedException e) {
                  e.printStackTrace();
               } catch (ExecutionException e) {
                  e.printStackTrace();
               }
            return new Date().getTime();
         }
      static class  MyTask extends AsyncTask<String, Void, Date> {
            @Override
            protected Date doInBackground(String... params) {
                  URL url;
                  try {
                        url = new URL(params[0]);
                        URLConnection uc = url.openConnection();
                        uc.connect();
                        long ld = uc.getDate();
                        Date date = new Date(ld);
                        return date;
                     } catch (MalformedURLException e) {
                        e.printStackTrace();
                     } catch (IOException e) {
                        e.printStackTrace();
                     }
                  return new Date();
               }
            @Override
            protected void onPreExecute() {
               }
            @Override
            protected void onPostExecute(Date s) {
               }
         }
}
