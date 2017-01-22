package com.perfume.Utis;

import org.json.*;

public class Json
 {
      private static JSONObject js;//Js对象
      private static String buff;//数据缓冲区
      /**
       * JSon解析
       */
      public static String JsonPerson(String data, String type) {
            try {
                  js = new JSONObject(data.replace("VideoList [","").replace("]",""));
                  buff = js.getString(type);
                  return buff;
               } catch (JSONException e) {
                  e.printStackTrace();
               }
            return "";
         }
   }
