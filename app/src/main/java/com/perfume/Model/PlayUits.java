package com.perfume.Model;

/*
视频信息模块
*/
public class PlayUits
{
      public  String Uri;   //播放连接
      public  String Cookie;//播放Cookie
      public  boolean IsPlay;//是否可以播放
      public  boolean IsVip; //是否是VIP视频
      public String Name;
      public String Picture;
      public static boolean Is=false;

      public void setPicture(String picture) {
            Picture = picture;
         }

      public String getPicture() {
            return Picture;
         }

      public String getName() {
            return Name;
         }

      public void setName(String name) {
            Name = name;
         }

      public  String getUri() {
            return Uri;
         }

      public  boolean getIsVip() {
            return IsVip;
         }

      public  void setUri(String uri) {
            Uri = uri;
         }

      public  void setIsVip(boolean isVip) {
            IsVip = isVip;
         }

      public  void setIsPlay(boolean isPlay) {
            IsPlay = isPlay;
         }

      public  boolean getIsPlay() {
            return IsPlay;
         }

      public  void setCookie(String cookie) {
            Cookie = cookie;
         }

      public  String getCookie() {
            return Cookie;
         }
}
