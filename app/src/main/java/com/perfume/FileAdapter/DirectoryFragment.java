package com.perfume.FileAdapter;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.canyinghao.candialog.*;
import com.perfume.Utis.*;
import com.perfume.Utis.Apktool.*;
import com.perfume.activity.*;
import java.io.*;
import java.util.*;

import Tool.apkeditor.apksigner.SignApk;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.StateSet;
import brut.androlib.res.AndrolibResources;
import brut.common.BrutException;
import Tool.apkeditor.apksigner.KeyHelper;
/*
文件浏览器
*/
public class DirectoryFragment extends Fragment {
    private View fragmentView;
    private boolean receiverRegistered = false;
    public static File currentDir;
    public static String odexPath;
    private ListView listView;
    private ListAdapter listAdapter;
    private TextView emptyView;
    private DocumentSelectActivityDelegate delegate;
    String iss="";
    private static String title_ = "";
    private ArrayList<ListItem> items = new ArrayList<ListItem>();
    private ArrayList<HistoryEntry> history = new ArrayList<HistoryEntry>();
    private HashMap<String, ListItem> selectedFiles = new HashMap<String, ListItem>();
    private long sizeLimit = 1024 * 1024 * 1024;
    
    private class HistoryEntry {
        int scrollItem, scrollOffset;
        File dir;
        String title;
    }
    public static abstract interface DocumentSelectActivityDelegate {
        public void didSelectFiles(DirectoryFragment activity, ArrayList<String> files);
        public void startDocumentSelectActivity();
        public void updateToolBarName(String name);
    }
    public boolean onBackPressed_() {
        if (history.size() > 0) {
            HistoryEntry he = history.remove(history.size() - 1);
            title_ = he.title;
            updateName(title_);
            if (he.dir != null) {
                listFiles(he.dir);
            } else {
                listRoots();
            }
            listView.setSelectionFromTop(he.scrollItem, he.scrollOffset);
            return false;
        } else {
            return true;
        }
    }
    private void updateName(String title_) {
        if (delegate != null) {
            delegate.updateToolBarName(title_);
        }
    }
    public void onFragmentDestroy() {
        try {
            if (receiverRegistered) {
                getActivity().unregisterReceiver(receiver);
            }
        } catch (Exception e) {
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Runnable r = new Runnable() {
                public void run() {
                      undara();
                }
            };
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                listView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    };
    public void setDelegate(DocumentSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    private class ListItem {
        int icon;
        String title;
        String subtitle = "";
        String ext = "";
        String thumb;
        File file;
        int type=0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        AndrolibResources.setr9s ( new Res9 ( ) );
        File odex = getActivity().getDir ( "cache",getActivity(). MODE_PRIVATE );
        odexPath  = odex.getAbsolutePath ( );
        AssetsFile.baa ( );
        Shell.k ( );
        Print p=new Print(h);
        brut.apktool.Main.setLog(p);
        if ( Build.VERSION.SDK_INT < 21 )
          {
            String[]  aimpath =new String[]{"if","/system/framework/framework-res.apk"};
            try
              {
                brut.apktool.Main.main ( aimpath );
              }
            catch (InterruptedException e)
              {}
            catch (BrutException e)
              {}
            catch (IOException e)
              {}
          }
        if (!receiverRegistered) {
            receiverRegistered = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
            filter.addAction(Intent.ACTION_MEDIA_CHECKING);
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_NOFS);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_SHARED);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            filter.addDataScheme("file");
            getActivity().registerReceiver(receiver, filter);
        }
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.document_select_layout, container, false);
            TextDetailDocumentsCell.c=getActivity();
            listAdapter = new ListAdapter(getActivity());
            emptyView = (TextView) fragmentView.findViewById(R.id.searchEmptyView);
            emptyView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            listView = (ListView) fragmentView.findViewById(R.id.listView);
            listView.setEmptyView(emptyView);
            listView.setAdapter(listAdapter);
            listView.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener ( ){
                  @Override
                  public boolean onItemLongClick ( AdapterView<?> p1, View p2, int p3, long p4 )
                    {
                      if (p3< 0 || p3 >= items.size()) {
                          return true;
                        }
                      final ListItem item = items.get(p3);
                      final File file = item.file;
                      new CanDialog.Builder(getActivity())
                        .setTitle(getActivity().getString(R.string.app_name))
                        .setItems(new String[]{"重命名", "删除","压缩"}, new CanDialogInterface.OnClickListener() {
                            @Override
                            public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
                               if(position==0)
                               {
                                 new CanDialog.Builder(getActivity())
                                   .setIconType(CanDialog.ICON_INFO)
                                   .setTitle("文件名："+file.getName())
                                   .setEditDialog("输入新的文件名", false,1, 0)
                                   .setNegativeButton("取消", true, null)
                                   .setPositiveButton("确定", false, new CanDialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
                                           File f=new File(file.getAbsolutePath());
                                           f.renameTo(new File(file.getParent()+"/"+text));
                                           dialog.setAnimationMessage(CanDialog.ANIM_INFO_SUCCESS, "重命名成功 " + text.toString());
                                           dialog.setPositiveButton("确定", true, null);
                                           undara();
                                         }
                                     })
                                   .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
                                   .setCancelable(true)
                                   .show();
                               }
                               if(position==1)
                               {
                                 final File f=new File(file.getAbsolutePath());
                                 new CanDialog.Builder(getActivity())
                                   .setIconType(CanDialog.ICON_WARNING)
                                   .setTitle(getActivity().getString(R.string.app_name))
                                   .setMessage("确定要删除："+file.getName())
                                   .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
                                   .setNegativeButton("取消", true, null)
                                   .setPositiveButton ( "确定", true, new CanDialogInterface.OnClickListener ( ){

                                       @Override
                                       public void onClick ( CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems )
                                         {
                                          AssetsFile. DeleteFile(f);
                                          ShowDialog. showErrorBox(getActivity(),"成功删除"+f.getName());
                                          undara();
                                         }
                                     } )
                                   .show();
                               }
                               if(position==2)
                               {
                                 String ji;
                                 if(file.getName().toString().indexOf(".")!=-1)
                                 {
                                   ji=file.getName().toString().subSequence(0,file.getName().indexOf(".")).toString();
                                   //  ZipUtil.packEntry(new File(file.getAbsolutePath()),new File(file.getAbsolutePath().subSequence(0 ,file.getAbsolutePath().length() - file.getName().length()).toString()+"/"+ji+".zip"));
                                 }
                                 if(file.getName().toString().indexOf(".")==-1)
                                 {
                                   //ZipUtil.pack(new File( file.getAbsolutePath()),new File(file.getAbsolutePath().subSequence(0,file.getAbsolutePath().length()-file.getName().length()).toString()+file.getName()+".zip"));
                                 }
                                ShowDialog. showErrorBox(getActivity(),"压缩完成");
                                  undara();
                               }
                                dialog.dismiss();
                              }
                          })
                        .setCancelable(true)
                        .setFullBackgroundColor(Color.TRANSPARENT)
                        .setCircularRevealAnimator(CanDialog.CircularRevealStatus.TOP_LEFT)
                        .show();
                      return true;
                    }
                } );
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,int i, long l) {
                    if (i < 0 || i >= items.size()) {
                        return;
                    }
                    final ListItem item = items.get(i);
                    final File file = item.file;
                    if (file == null) {
                        HistoryEntry he = history.remove(history.size() - 1);
                        title_ = he.title;
                        updateName(title_);
                        if (he.dir != null) {
                            listFiles(he.dir);
                        } else {
                            listRoots();
                        }
                        listView.setSelectionFromTop(he.scrollItem,he.scrollOffset);
                    } else if (file.isDirectory()) {
                        if(file.getName().toString().indexOf("_smali")!=-1)
                          {
                            new CanDialog.Builder(getActivity())
                              .setTitle("选择操作类型：")
                              .setSingleChoiceItems(new String[]{"编译为dex", "浏览"}, 0, new CanDialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
                                    }
                                })
                              .setNegativeButton("取消", true, null)
                              .setPositiveButton("确定", true, new CanDialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems) {
                                      if(checkItem==0)
                                        {
                                          String[] ba=new String[]{file.getAbsolutePath(),"-o",file.getAbsolutePath().replace("_smali",".dex")};
                                          org.jf.smali.main. main ( ba );
                                          ShowDialog.showErrorBox(getActivity(),"编译完成");
                                          undara();
                                        }
                                      if(checkItem==1)
                                        {
                                          HistoryEntry he = new HistoryEntry();
                                          he.scrollItem = listView.getFirstVisiblePosition();
                                          he.scrollOffset = listView.getChildAt(0).getTop();
                                          he.dir = currentDir;
                                          he.title = title_.toString();
                                          updateName(title_);
                                          if (!listFiles(file)) {
                                              return;
                                            }
                                          history.add(he);
                                          title_ = item.title;
                                          updateName(title_);
                                          listView.setSelection(0);
                                        }
                                    }
                                })
                              .setTileAnimator()
                              .setCancelable(true)
                              .show();
                              return;
                              }                     
                      if(file.getName().toString().indexOf("_src")!=-1)
                      {
                        new CanDialog.Builder(getActivity())
                          .setTitle("选择操作类型：")
                          .setSingleChoiceItems(new String[]{"编译为APK", "浏览","ApkTool Project"}, 0, new CanDialogInterface.OnClickListener() {
                              @Override
                              public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
                               }
                            })
                          .setNegativeButton("取消", true, null)
                          .setPositiveButton("确定", true, new CanDialogInterface.OnClickListener() {
                              @Override
                              public void onClick(CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems) {
                                  if(checkItem==0)
                                  {
                                    ShowDialog.showLogin(getActivity(),"编译为APK");
                                    new Thread ( ){
                                        public void run ( )
                                          {
                                            Message a=new Message();
                                            a.what=404;
                                            String[] main=new String[] {"b","-f","-a",odexPath+"/aapt",file.getAbsolutePath(),"-o",file.getAbsolutePath().replace ( "_src", ".apk" )};
                                            try
                                              {
                                                brut.apktool.Main.main ( main );
                                              }
                                            catch (BrutException e)
                                              {
                                                 a.obj=e.toString();
                                                 h.sendMessage(a);
                                              }
                                            catch (IOException e)
                                              {
                                                a.obj=e.toString();
                                                 h.sendMessage(a);
                                              }
                                            catch (InterruptedException e)
                                              {
                                                a.obj=e.toString();
                                                h.sendMessage(a);
                                              }
                                            h.sendEmptyMessage(101);
                                              }
                                              }.start();
                                  }
                                if(checkItem==1)
                                  {
                                    HistoryEntry he = new HistoryEntry();
                                    he.scrollItem = listView.getFirstVisiblePosition();
                                    he.scrollOffset = listView.getChildAt(0).getTop();
                                    he.dir = currentDir;
                                    he.title = title_.toString();
                                    updateName(title_);
                                    if (!listFiles(file)) {
                                        return;
                                      }
                                    history.add(he);
                                    title_ = item.title;
                                    updateName(title_);
                                    listView.setSelection(0);
                                  }
                                  if(checkItem == 2)
                                  {
                                     Intent a=new Intent(getActivity(),ApktoolProject.class);
                                     a.putExtra("Path",file.getAbsolutePath());
                                     startActivity(a);
                                  }
                                }
                            })
                            .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
                            //  .setTileAnimator()
                          .setCancelable(true)
                          .show();
                          return;
                      }
                      try
                      {
                         HistoryEntry he = new HistoryEntry();
                         he.scrollItem = listView.getFirstVisiblePosition();
                         he.scrollOffset = listView.getChildAt(0).getTop();
                         he.dir = currentDir;
                         he.title = title_.toString();
                         updateName(title_);
                         if (!listFiles(file)) {
                               return;
                            }
                         history.add(he);
                         title_ = item.title;
                         updateName(title_);
                         listView.setSelection(0);
                      }catch(Exception e)
                      {
                         ShowDialog.showErrorBox(getActivity(),"AccessError");
                      }
                        
                    } else {
                        if (!file.canRead()) {
                          ShowDialog.showErrorBox(getActivity(),"AccessError");
                            return;
                        }
                        if (sizeLimit != 0) {
                            if (file.length() > sizeLimit) {
                               ShowDialog.showErrorBox(getActivity(),"FileUploadLimit");
                                return;
                            }
                        }
                        if (file.length() == 0) {
                            return;
                       }
                  if(file.getName().endsWith(".apk")||file.getName().endsWith(".dex"))
                          {
                             if(file.getName().endsWith(".apk"))
                             {
                                showBox(file.getAbsolutePath(),1);
                             }else
                             {
                             showBox(file.getAbsolutePath(),2);
                             }
                          }else
                            if(file.getName().endsWith(".java")
                               ||file.getName().endsWith(".txt") 
                               ||file.getName().endsWith(".smali") 
                               ||file.getName().endsWith(".c") 
                               ||file.getName().endsWith(".cpp") 
                               ||file.getName().endsWith(".class")
                               ||file.getName().endsWith(".yml")
                               ||file.getName().endsWith(".xml")
                               ||file.getName().endsWith(".cc")
                               )
                              { 
                                Intent intent=new Intent(getActivity(), XmlSourceViewerActivity.class);
                                intent.putExtra("Path",file.getAbsolutePath());
                                XmlSourceViewerActivity.isfile=true;
                                startActivity(intent);
                              }
                          else
                          if (file.getName().endsWith(".jpg")
                              || file.getName().endsWith(".png")
                              || file.getName().endsWith(".gif")
                              || file.getName().endsWith(".jpeg")
                              || file.getName().endsWith(".bmp")
                              ) {
                                ImageViewer.Path=file.getAbsolutePath();
                                startActivity(new Intent(getActivity(),ImageViewer.class));
                                return;
                             }
                             else
                            if(file.getName().endsWith(".zip"))
                            {
                               Intent a=new Intent(getActivity(),ZipView.class);
                               ZipView.zipFileName=file.getAbsolutePath();
                               startActivity(a);
                            }
                            else     
                          {
                             MiniType.openFiles(getActivity(),file.getAbsolutePath());
                          }
                            return;
                        }
                }
            });
            listRoots();
        } else {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }
        return fragmentView;
    }

    private void listRoots() {
        currentDir = null;
        items.clear();
        String extStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        ListItem ext = new ListItem();
        if (Build.VERSION.SDK_INT < 9|| Environment.isExternalStorageRemovable()) {
            ext.title = "SdCard";
        } else {
            ext.title = "InternalStorage";
        }
        ext.icon = Build.VERSION.SDK_INT < 9|| Environment.isExternalStorageRemovable() ? R.drawable.ic_external_storage: R.drawable.ic_storage;
        ext.subtitle = getRootSubtitle(extStorage);
        ext.file = Environment.getExternalStorageDirectory();
        items.add(ext);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
            ArrayList<String> result = new ArrayList<String>();
            String extDevice = null;
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("/mnt") && !line.contains("/storage") && !line
                        .contains("/sdcard"))
                        || line.contains("asec")
                        || line.contains("tmpfs") || line.contains("none")) {
                    continue;
                }
                String[] info = line.split(" ");
                if (!aliases.containsKey(info[0])) {
                    aliases.put(info[0], new ArrayList<String>());
                }
                aliases.get(info[0]).add(info[1]);
                if (info[1].equals(extStorage)) {
                    extDevice = info[0];
                }
                result.add(info[1]);
            }
            reader.close();
            if (extDevice != null) {
                result.removeAll(aliases.get(extDevice));
                for (String path : result) {
                    try {
                        ListItem item = new ListItem();
                        if (path.toLowerCase().contains("sd")) {
                            ext.title = "SdCard";
                        } else {
                            ext.title = "ExternalStorage";
                        }
                        item.icon = R.drawable.ic_external_storage;
                        item.subtitle = getRootSubtitle(path);
                        item.file = new File(path);
                        items.add(item);
                    } catch (Exception e) {
                        
                    }
                }
            }
        } catch (Exception e) {
           
        }
        ListItem fs = new ListItem();
        fs.title = "/";
        fs.subtitle = "SystemRoot";
        fs.icon = R.drawable.ic_directory;
        fs.file = new File("/");
        items.add(fs);
        listAdapter.notifyDataSetChanged();
    }

    private boolean listFiles(File dir) {
        if (!dir.canRead()) {
            if (dir.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString())|| dir.getAbsolutePath().startsWith("/sdcard")|| dir.getAbsolutePath().startsWith("/mnt/sdcard")) {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    currentDir = dir;
                    items.clear();
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_SHARED.equals(state)) {
                        emptyView.setText("UsbActive");
                    } else {
                        emptyView.setText("NotMounted");
                    }
                    clearDrawableAnimation(listView);
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            }
          ShowDialog.showErrorBox(getActivity(),"AccessError");
            return false;
        }
        emptyView.setText("NoFiles");
        File[] files = null;
        try {
            files = dir.listFiles();
        } catch (Exception e) {
              ShowDialog.showErrorBox(getActivity(), e.getLocalizedMessage());
            return false;
        }
        if (files == null) {
              ShowDialog.showErrorBox(getActivity(),"UnknownError");
            return false;
        }
        currentDir = dir;
        items.clear();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }
            ListItem item = new ListItem();
            item.title = file.getName();
            item.file = file;
            if (file.isDirectory()) {
                item.icon = R.drawable.ic_directory;
                item.subtitle = "Folder";
                if(file.getName().indexOf("_src")!=-1)
                     {
                        item.icon = R.mipmap.ic_launcher;
                        item.subtitle = "Apktool Project";
                     }
            } else{
                String fname = file.getName();
                String[] sp = fname.split("\\.");
                item.ext = sp.length > 1 ? sp[sp.length - 1] : "?";
                item.subtitle = formatFileSize(file.length());
                fname = fname.toLowerCase();
                if (fname.endsWith(".jpg") || fname.endsWith(".png")|| fname.endsWith(".gif") || fname.endsWith(".jpeg")) {
                   item.type=200;
                   item.thumb = file.getAbsolutePath();
                }
                if(fname.endsWith(".apk"))
                {
                   item.type=100;
                   item.thumb = file.getAbsolutePath();
                }
            }
            items.add(item);
        }
        ListItem item = new ListItem();
        item.title = "...";
        item.subtitle = "Folder";
        item.icon = R.drawable.ic_directory;
        item.file = null;
        items.add(0, item);
        clearDrawableAnimation(listView);
        listAdapter.notifyDataSetChanged();
        return true;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable = null;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }
/*
       SharedPreferences sharedata = getSharedPreferences ( "data", 0 );
*/
    
    public void showBox(final String error,final int i) {
        if (getActivity() == null) {
            return;
          }
        new CanDialog.Builder(getActivity())
          .setTitle("选择操作类型：")
          .setSingleChoiceItems(new String[]{"反编译", "反编译资源", "反编译dex","安装","签名"}, 0, new CanDialogInterface.OnClickListener() {
              @Override
              public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
                }
            })
          .setNegativeButton("取消", true, null)
          .setPositiveButton("确定", true, new CanDialogInterface.OnClickListener() {
              @Override
              public void onClick(CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems) {
                  if(checkItem==0)
                    {
                       if(i!=1)
                       {
                          ShowDialog.showErrorBox(getActivity(),"该文件不是APK");
                          return;
                       }
                       ShowDialog.showLogin(getActivity(),"反编译");
                      new Thread ( ){
                          public void run ( )
                            {
                               Message a=new Message();
                               a.what=404;
                              String[] main=new String[] {"d","-f","-b",error,"-o",error.replace(".apk","_src")};
                              try
                                {
                                  brut.apktool.Main.main ( main );
                                }
                              catch (BrutException e)
                                {
                                   a.obj=e.getMessage();
                                   h.sendMessage(a);
                                }
                              catch (IOException e)
                                {
                                   a.obj=e.getMessage();
                                   h.sendMessage(a);
                                }
                              catch (InterruptedException e)
                                {
                                   a.obj=e.getMessage();
                                   h.sendMessage(a);
                                }
                              CrashCatch.stringtosd("1",error.replace(".apk","_src")+"/ProjectType.txt");
                              h.sendEmptyMessage(101);
                            }
                        }.start();
                    }
                  if(checkItem==1)
                  {
                     if(i!=1)
                        {
                           ShowDialog.showErrorBox(getActivity(),"该文件不是APK");
                           return;
                        }
                    ShowDialog.showLogin(getActivity(),"反编译资源");
                    new Thread ( ){
                        public void run ( )
                          {
                             Message a=new Message();
                             a.what=404;
                            String[] main=new String[] {"d","-f","-s",error,"-o",error.replace(".apk","_src")};
                            try
                              {
                                brut.apktool.Main.main ( main );
                              }
                            catch (BrutException e)
                              {
                                 a.obj=e.getMessage();
                                 h.sendMessage(a);
                              }
                            catch (IOException e)
                              {
                                 a.obj=e.getMessage();
                                 h.sendMessage(a);
                              }
                            catch (InterruptedException e)
                              {
                                 a.obj=e.getMessage();
                                 h.sendMessage(a);
                              }
                             CrashCatch.stringtosd("2",error.replace(".apk","_src")+"/ProjectType.txt");
                            h.sendEmptyMessage(101);                            
                              }
                              }.start();
                  }
                  
                  if(checkItem==2)
                  {
                    if(error.endsWith(".dex"))
                    {
                      String[] bu=new String[]{"-o",error.replace(".dex","_smali"),error};
                      try
                        {
                          org.jf.baksmali.main.main ( bu );
                        }
                      catch (IOException e)
                        {}
                       ShowDialog.showErrorBox(getActivity(),"反编译dex完成");
                       undara();
                    }
                    if(error.endsWith(".apk"))
                    {
                       ShowDialog.showLogin(getActivity(),"反编译dex");
                       new Thread ( ){
                             public void run ( )
                                {
                       Message a=new Message();
                       a.what=404;
                       String[] bu=new String[]{"d",error,"-o",error.replace(".apk","_src"),"-f","-r","-c"};
                       try
                          {
                             brut.apktool.Main.main ( bu );
                          }
                       catch (BrutException e)
                          {
                             a.obj=e.getMessage();
                             h.sendMessage(a);
                          }
                       catch (IOException e)
                          {
                             a.obj=e.getMessage();
                             h.sendMessage(a);
                          }
                       catch (InterruptedException e)
                          {
                             a.obj=e.getMessage();
                             h.sendMessage(a);
                          }
                       CrashCatch.stringtosd("3",error.replace(".apk","_src")+"/ProjectType.txt");
                       h.sendEmptyMessage(101);
                       }
                       }.start();
                    }
                  
                      }
                  if(checkItem==3)
                  { 
                     if(i!=1)
                        {
                           ShowDialog.showErrorBox(getActivity(),"该文件不是APK");
                           return;
                        }
                  AssetsFile.b(getActivity(),error);
                  }
                  if(checkItem==4)
                  {
                     if(i!=1)
                        {
                           ShowDialog.showErrorBox(getActivity(),"该文件不是APK");
                           return;
                        }
                     SignApk signApk = new SignApk(KeyHelper. privateKey, KeyHelper. sigPrefix);
                     boolean signed= signApk.sign(error, error.replace(".apk","_sign.apk"));
                     if (signed){                          
                           try
                              {
                                 SignApk.verifyJar ( error.replace ( ".apk", "_sign.apk" ) );
                              }
                           catch (Exception e)
                              {}
                        }
                  ShowDialog.showErrorBox(getActivity(),"签名完成");
                  undara();
                  }
                }
            })
          .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
        //  .setTileAnimator()
          .setCancelable(true)
          .show();
      }
      
      
      
      
    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
        long free = (long) stat.getAvailableBlocks()  * (long) stat.getBlockSize();
        if (total == 0) {
            return "";
        }
        return "Free " + formatFileSize(free) + " of " + formatFileSize(total);
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;
        public ListAdapter(Context context) {
            mContext = context;
        }
        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        public int getViewTypeCount() {
            return 2;
        }
        public int getItemViewType(int pos) {
            return items.get(pos).subtitle.length() > 0 ? 0 : 1;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextDetailDocumentsCell(mContext);
            }
            TextDetailDocumentsCell textDetailCell = (TextDetailDocumentsCell) convertView;
            ListItem item = items.get(position);
            if (item.icon != 0) {
                ((TextDetailDocumentsCell) convertView).setTextAndValueAndTypeAndThumb(item.title,item.subtitle, null, null, item.icon);
            } else if(item.type!=0)
            {
               ((TextDetailDocumentsCell) convertView) .setTextAndValueAndTypeAndThumb(item.title, item.subtitle, null, item.thumb, item.type);
            }else{
                String type = item.ext.toUpperCase().substring(0,Math.min(item.ext.length(), 4));
                ((TextDetailDocumentsCell) convertView) .setTextAndValueAndTypeAndThumb(item.title, item.subtitle, type, item.thumb, 0);
            }
            return convertView;
        }
    }
    
    
    
    
    
    
    
    
    
    public void undara()
    {
       try {
             if (currentDir == null) {
                   listRoots();
                } else {
                   listFiles(currentDir);
                }
          } catch (Exception e) {
          }
    }
    Handler h =new Handler ( )
      {
        @Override
        public void handleMessage ( Message msg )
          {
            switch ( msg.what )
              {
                case 101:
                  com.zhl.cbdialog.CBDialogBuilder.dialog.dismiss ( );
                  ShowDialog.showErrorBox(getActivity(),iss);
                  undara();
                  iss="";
                  break;
                case 100:
                  iss=iss+"输出信息..\n"+msg.obj.toString();
                  break;
                case 404:
                    com.zhl.cbdialog.CBDialogBuilder.dialog.dismiss ( );
                    ShowDialog.showErrorBox(getActivity(),msg.obj.toString());
                  break;
              }
          }
      };
    void print(String s)
      {
        Message m=new Message();
        m.what=100;
        m.obj=s+"\n";
        h.sendMessage(m);
      }
    class Print implements brut.androlib.Log
      {
        @Override
        public void write_out(byte[] p1) throws IOException
          {
            String msg=new String(p1);
            Message m=new Message();
            m.what=100;
            m.obj=msg;
            h.sendMessage(m);
          }

        @Override
        public void write_err(byte[] p1) throws IOException
          {
            String msg=new String(p1);
            Message m=new Message();
            m.what=100;
            m.obj=msg;
            h.sendMessage(m);
          }
        Handler h;
        Print(Handler h)
          {
            this.h=h;
          }
      }
}
