package com.perfume.Model;

import java.util.*;
/*
添加视频模块
*/
public class VideoList
{
      private static List<String> keyList;
      /**
       * 服务器数据列表
       * @return
       */
      public static List<String> VideoInfo()
         {
            keyList = new ArrayList<>();
            keyList.add("Uri");
            keyList.add("Cookie");
            keyList.add("IsPlay");
            keyList.add("IsVip");
            keyList.add("Name");
            keyList.add("Picture");
            return keyList;
         }
}
