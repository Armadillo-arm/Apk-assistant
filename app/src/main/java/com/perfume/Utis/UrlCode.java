package com.perfume.Utis;

import java.security.*;
import org.json.*;

import java.io.UnsupportedEncodingException;
/*
云服务器解析操作类
*/
public class UrlCode
 {
      public static final int NETWORKTYPE_2G = 2;
      public static final int NETWORKTYPE_3G = 3;
      public static final int NETWORKTYPE_4G = 5;
      public static final int NETWORKTYPE_INVALID = 0;
      public static final int NETWORKTYPE_UNKNOWN = 6;
      public static final int NETWORKTYPE_WAP = 1;
      public static final int NETWORKTYPE_WIFI = 4;
      public static String decode(String unicodeStr) {
            if (unicodeStr == null) {
                  return null;
               }
            StringBuffer retBuf = new StringBuffer();
            int maxLoop = unicodeStr.length();
            for (int i = 0; i < maxLoop; i++) {
                  if (unicodeStr.charAt(i) == '\\') {
                        if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                           try {
                                 retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                                 i += 5;
                              } catch (NumberFormatException localNumberFormatException) {
                                 retBuf.append(unicodeStr.charAt(i));
                              }
                        else
                           retBuf.append(unicodeStr.charAt(i));
                     } else {
                        retBuf.append(unicodeStr.charAt(i));
                     }
               }
            return retBuf.toString();
         }
      public static String Getname(String str, String str2, String str3, int i) {
            String str4 = "";
            switch (i) {
                  case NETWORKTYPE_WAP /*1*/:
                     try {
                           str4 = new JSONObject(str).getString(str3);
                           break;
                        } catch (Exception e) {
                           e.printStackTrace();
                           return "";
                        }
                  case NETWORKTYPE_2G /*2*/:
                     try {
                           str4 = new JSONObject(str).getJSONObject(str2).getString(str3);
                           break;
                        } catch (Exception e2) {
                           e2.printStackTrace();
                           return "";
                        }
                  case NETWORKTYPE_3G /*3*/:
                     try {
                           JSONArray jSONArray = new JSONObject(str).getJSONArray(str2);
                           int i2 = 0;
                           while (i2 < jSONArray.length()) {
                                 str4 = i2 == 0 ? jSONArray.getJSONObject(i2).getString(str3) : str4 + "\n" + jSONArray.getJSONObject(i2).getString(str3);
                                 i2++;
                              }
                           break;
                        } catch (Exception e22) {
                           e22.printStackTrace();
                           return "";
                        }
               }
            return str4;
         }
      public static String GetMD5(String string) {
            byte[] hash;
            String a = string + "gaoshini";
            try {
                  hash = MessageDigest.getInstance("MD5").digest(a.getBytes("UTF-8"));
               } catch (NoSuchAlgorithmException e) {
                  e.printStackTrace();
                  return null;
               } catch (UnsupportedEncodingException e) {
                  e.printStackTrace();
                  return null;
               }

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                  if ((b & 0xFF) < 0x10)
                     hex.append("0");
                  hex.append(Integer.toHexString(b & 0xFF));
               }

            return hex.toString().trim().toUpperCase();
         }
      public static boolean Getmsg(String JSON) {
            try {
                  JSONTokener jsonParser = new JSONTokener(JSON);
                  JSONObject person = (JSONObject) jsonParser.nextValue();
                  if (person.getString("msg").equals("ok")) {
                        return true;
                     } else if (person.getString("msg").equals("error")) {
                        return false;
                     } else {
                        return false;
                     }
               } catch (JSONException ex) {
               }
            return false;
         }
      public static String GetUrl2(String JSON) {
            try {
                  JSONTokener jsonParser = new JSONTokener(JSON);
                  JSONObject person = (JSONObject) jsonParser.nextValue();
                  return person.getString("url");
               } catch (JSONException ex) {
               }
            return "";
         }
   }

