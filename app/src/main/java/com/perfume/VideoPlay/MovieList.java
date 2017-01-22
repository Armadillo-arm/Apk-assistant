package com.perfume.VideoPlay;

import android.content.*;
import android.os.*;
import android.view.*;
import java.io.*;
import java.net.*;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AdapterView;
import com.perfume.R;
import com.perfume.UI.LoadListView;
import com.perfume.Utis.UrlCode;
import com.perfume.activity.VodInfo;
import com.perfume.adapter.ItemList;
import java.util.ArrayList;
import com.perfume.Utis.ShowDialog;

/**
 * 电影
 */
public class MovieList extends Fragment implements LoadListView.ILoadListener, SwipeRefreshLayout.OnRefreshListener {
      private SwipeRefreshLayout mSwipeRefreshWidget;
      private View mView;
      private StringBuffer buff = new StringBuffer();
      private int ii = 1;
      private ArrayList<ItemEntity> list = new ArrayList<>();
      private LoadListView listView;
      private URL u;
      private HttpURLConnection i;
      private InputStream in;
      private InputStreamReader inr;
      private BufferedReader stb;

      @Nullable
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if (mView == null) {
                  mView = inflater.inflate(R.layout.fragment_cheese_list, container, false);
                  listView = (LoadListView) mView.findViewById(R.id.recyclerView);
                  mSwipeRefreshWidget = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
                  mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
                  mSwipeRefreshWidget.setSize(SwipeRefreshLayout.LARGE);
                  mSwipeRefreshWidget.setOnRefreshListener(this);
                  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                 Context context = getContext();
                                 Intent intent = new Intent(context, VodInfo.class);
                                 intent.putExtra(VodInfo.EXTRA_NAME, list.get(i).getName());
                                 intent.putExtra(VodInfo.EXTRA_IMAGE, list.get(i).getTb());
                                 intent.putExtra(VodInfo.EXTRA_MSG, list.get(i).getMsg());
                                 intent.putExtra(VodInfo.EXTRA_PINFENG, list.get(i).getPingfen());
                                 intent.putExtra(VodInfo.EXTRA_YANYUAN, list.get(i).getYanyuan());
                                 intent.putExtra(VodInfo.EXTRA_URL, list.get(i).getUrl());
                                 context.startActivity(intent);
                              }
                        });
                  Undata();

               }
            return mView;
         }

      private void Undata() {
            new Thread() {
                  @Override
                  public void run() {
                        try {
                              u = new URL("http://search.video.iqiyi.com/o?pageNum=1&mode=11&ctgName=%e7%94%b5%e5%bd%b1&threeCategory=&pageSize=21&type=list&if=html5&pos=1&site=");
                              i = (HttpURLConnection) u.openConnection();
                              in = i.getInputStream();
                              inr = new InputStreamReader(in);
                              stb = new BufferedReader(inr);
                              String line;
                              while ((line = stb.readLine()) != null) {
                                    buff.append(UrlCode.decode(line));
                                 }
                              stb.close();
                              inr.close();
                              in.close();
                              h.sendEmptyMessage(1);
                           } catch (MalformedURLException e) {
                              e.printStackTrace();
                           } catch (IOException e) {
                              e.printStackTrace();
                           }
                     }
               }.start();
         }

      Handler h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                  switch (msg.what) {
                        case 1:
                           if (getData()) {
                                 showListView(list);
                              }
                           break;
                        case 2:
                           if (getData()) {
                                 showListView(list);
                                 listView.loadComplete();
                              } else {
                                 listView.loadComplete();
                              }
                           mSwipeRefreshWidget.setRefreshing(false);
                           break;
                     }
               }
         };
      private ItemList adapter;

      private void showListView(ArrayList<ItemEntity> list) {
            if (adapter == null) {
                  listView.setInterface(this);
                  adapter = new ItemList(getActivity(), list);
                  listView.setAdapter(adapter);
               } else {
                  adapter.onDateChange(list);
               }
         }

      private boolean getData() {
            try {
                  String[] a = UrlCode.Getname(UrlCode.Getname(buff.toString(), "", "data", 1), "docinfos", "albumDocInfo", 3).split("\n");
                  for (int i = 0; i < a.length; i++) {
                        String b = UrlCode.Getname(a[i], "", "albumTitle", 1); //名字
                        String image = UrlCode.Getname(a[i], "", "albumHImage", 1);//图片
                        String yanyuan = UrlCode.Getname(a[i], "", "star", 1);//演员
                        String pingfen = UrlCode.Getname(a[i], "", "score", 1);//评分
                        String url = UrlCode.Getname(a[i], "", "albumLink", 1);//连接
                        String xxx = UrlCode.Getname(UrlCode.Getname(a[i], "", "video_lib_meta", 1), "", "description", 1);//信息
                        ItemEntity entity = new ItemEntity();
                        entity.setName(b);
                        entity.setPingfen(pingfen);
                        entity.setTb(image);
                        entity.setYanyuan(yanyuan);
                        entity.setUrl(url);
                        entity.setMsg(xxx);
                        list.add(entity);
                     }
                  buff.delete(0, buff.length());
                  return true;
               } catch (Exception e) {
                  ShowDialog.showErrorBox(getActivity(), "数据解析出现异常");
                  return false;
               }
         }

      @Override
      public void onLoad() {
            mSwipeRefreshWidget.setRefreshing(true);
            new Thread() {
                  @Override
                  public void run() {
                        try {
                              ii++;
                              u = new URL("http://search.video.iqiyi.com/o?pageNum=" + ii + "&mode=11&ctgName=%e7%94%b5%e5%bd%b1&threeCategory=&pageSize=21&type=list&if=html5&pos=1&site=");
                              i = (HttpURLConnection) u.openConnection();
                              in = i.getInputStream();
                              inr = new InputStreamReader(in);
                              stb = new BufferedReader(inr);
                              String line;
                              while ((line = stb.readLine()) != null) {
                                    buff.append(UrlCode.decode(line));
                                 }
                              stb.close();
                              inr.close();
                              in.close();
                              h.sendEmptyMessage(2);
                           } catch (MalformedURLException e) {
                              e.printStackTrace();
                           } catch (IOException e) {
                              e.printStackTrace();
                           }
                     }
               }.start();
         }

      @Override
      public void onRefresh() {
            if (getData()) {
                  showListView(list);
               }
            mSwipeRefreshWidget.setRefreshing(false);
         }
   }

