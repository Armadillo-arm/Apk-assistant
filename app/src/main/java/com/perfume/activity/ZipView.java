package com.perfume.activity;


import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.canyinghao.candialog.*;
import com.perfume.*;
import com.perfume.Utis.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

import android.app.ListActivity;
import android.database.DataSetObserver;
import com.bumptech.glide.Glide;
import com.perfume.R;
import java.text.SimpleDateFormat;





/*
ZIP文件查看
*/
public class ZipView extends ListActivity  {

      public static final String EXTRACTPATH="/sdcard/ZipView";
      public static HashMap<String,byte[]> zipEnties;
      public Tree tree;
      private static ZipFile zipFile;
      public static String file;
      public static String zipFileName;
      private String title="";
      private String progress="";
      private boolean isSigne=false;
      private boolean isChanged=false;
      private FileListAdapter mAdapter;
      private List<String> fileList;

      private static Stack<String> path;
      private static int dep;


      private boolean is=false;
      private int mod;
      private static final int UNUSE=-1;
      private static final int WRITEZIP=0;
      private static final int SIGNED=1;
      private static final int SENDINTENT=2;
      private static final int ERROR=3;
      private static final int LOADING=4;
      private static final int REMOVE=5;


      private static final int OPENDIR=6;
      private static final int BACK=7;
      private static final int OTHER=8;

      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           // getActionBar().setTitle("Zip View");
          //  getActionBar().setSubtitle(zipFileName);
            
            init();
            mAdapter=new FileListAdapter(this);
            mAdapter.registerDataSetObserver(new DataSetObserver(){
                     @Override
                     public void onInvalidated() {
                           switch(mod){
                                 case OPENDIR:
                                    tree.push(file);
                                    fileList=tree.list();
                                    break;
                                 case BACK:
                                    tree.pop();
                                    fileList=tree.list();
                                    break;
                                 case OTHER:
                                    fileList=tree.list();
                                    break;
                              }
                           setTitle(title+tree.getCurPath());
                        }
                  });
            setListAdapter(mAdapter);
            registerForContextMenu(getListView());
         }
//      @Override
//      public void OnItemLongClickListener(ListView list,View v,int position,long id){
//            file= (String) list.getItemAtPosition(position);
//            if(tree.isDirectory(file)){
//                  new CanDialog.Builder(ZipView.this)
//                     .setTitle("选择操作类型：")
//                     .setSingleChoiceItems(new String[]{"删除", "解压"}, 0, new CanDialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(CanDialog dialog, int position, CharSequence text, boolean[] checkitems) {
//                              }
//                        })
//                     .setNegativeButton("取消", true, null)
//                     .setPositiveButton("确定", true, new CanDialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems) {
//                                 if(checkItem==0)
//                                    {
//                                       new CanDialog.Builder(ZipView.this)
//                                          .setIconType(CanDialog.ICON_WARNING)
//                                          .setTitle(ZipView.this.getString(R.string.app_name))
//                                          .setMessage("是否删除："+file)
//                                          .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
//                                          .setNegativeButton("取消", true, null)
//                                          .setPositiveButton ( "确定", true, new CanDialogInterface.OnClickListener ( ){
//                                                @Override
//                                                public void onClick ( CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems )
//                                                   {
//                                                      
//                                                     
//                                                      removeDirectory(file);
//                                                      tree=new Tree(zipEnties.keySet());
//                                                      mod=OTHER;
//                                                      mAdapter.notifyDataSetInvalidated();
//                                                      is=true;
//                                                   }
//                                             } )
//                                          .show();
//                                    }
//                                 if(checkItem==1)
//                                    {
//                                       String n=null;
//                                       try
//                                          {
//                                             n=extract( file );
//                                          }
//                                       catch (Exception e)
//                                          {}
//                                       ShowDialog.showErrorBox(ZipView.this,"解压成功\n文件路径:"+n);
//                                    }
//                                 
//                              }
//                        })
//                     .setTileAnimator()
//                     .setCancelable(true)
//                     .show();
//                  return;
//               }
//               else
//               {
//                  return;
//               }
 //     }
      @Override
      public void onListItemClick(ListView list,View v,int position,long id){
            file= (String) list.getItemAtPosition(position);
            if(tree.isDirectory(file)){
                  mod=OPENDIR;
                  mAdapter.notifyDataSetInvalidated();
                  return;
               }
            mod=UNUSE;
            new CanDialog.Builder(ZipView.this)
               .setTitle("选择操作类型：")
               .setSingleChoiceItems(new String[]{"删除", "解压", "查看"}, 0, new CanDialogInterface.OnClickListener() {
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
                                 new CanDialog.Builder(ZipView.this)
                                    .setIconType(CanDialog.ICON_WARNING)
                                    .setTitle(ZipView.this.getString(R.string.app_name))
                                    .setMessage("是否删除："+file)
                                    .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
                                    .setNegativeButton("取消", true, null)
                                    .setPositiveButton ( "确定", true, new CanDialogInterface.OnClickListener ( ){
                                          @Override
                                          public void onClick ( CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems )
                                             {
                                                
                                                removeFile(file);
                                                tree=new Tree(zipEnties.keySet());
                                                mod=OTHER;
                                                mAdapter.notifyDataSetInvalidated();
                                                is=true;
                                             }
                                       } )
                                    .show();
                              }
                           if(checkItem==1)
                              {
                                 String n=null;
                                 try
                                    {
                                     n=extract( file );
                                    }
                                 catch (Exception e)
                                    {}
                                 ShowDialog.showErrorBox(ZipView.this,"解压成功\n文件路径:"+n);
                                    }
                           if(checkItem==2)
                              {
                                 if(file.endsWith(".jpg")
                                    || file.endsWith(".png")
                                    || file.endsWith(".gif")
                                    || file.endsWith(".jpeg")
                                    || file.endsWith(".bmp"))
                               {
                                  ImageViewer.ImageBuff=readEntry(file);
                                  startActivity(new Intent(ZipView.this,ImageViewer.class));
                                  return;
                               }
                               String b=new String(readEntry(file));
                               Intent a=new Intent(ZipView.this,XmlSourceViewerActivity.class);
                               XmlSourceViewerActivity.isfile=false;
                               XmlSourceViewerActivity.buff=b;
                               startActivity(a);
                              }
                          
                        }
                  })
               .setTileAnimator()
               .setCancelable(true)
               .show();
         }
      private void init(){
            title=zipFileName.substring(zipFileName.lastIndexOf("/")+1)+"/";
            unZip(zipFileName);
            tree=new Tree(zipEnties.keySet());
            setTitle(title+tree.getCurPath());
            fileList=tree.list();
         }
      private void unZip(String name) {
            if(zipEnties !=null)
               return;
            zipEnties =new HashMap<String,byte[]>();
            try{
                  zipFile=new ZipFile(name);
                  readZip(zipFile,zipEnties);
               }catch(IOException e){
                  zipEnties.put(e.getMessage(),null);
               }
         }



      @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                  if(!getTitle().equals(title)){
                        mod=BACK;
                        mAdapter.notifyDataSetInvalidated();
                        return true;
                     }else{
                        if(is){
                            new CanDialog.Builder(ZipView.this)
                                    .setIconType(CanDialog.ICON_WARNING)
                                    .setTitle(ZipView.this.getString(R.string.app_name))
                                    .setMessage("文件已被修改是否保存")
                                    .setCircularRevealAnimator(CanDialog.CircularRevealStatus.BOTTOM_RIGHT)
                                    .setNegativeButton ( "No", true, new CanDialogInterface.OnClickListener ( ){

                                          @Override
                                          public void onClick ( CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems )
                                             {
                                                finish();
                                             }
                                       } )
                                    .setPositiveButton ( "Yes", true, new CanDialogInterface.OnClickListener ( ){
                                          @Override
                                          public void onClick ( CanDialog dialog, int checkItem, CharSequence text, boolean[] checkItems )
                                             {
                                                saveFile();
                                             }
                                       } )
                                    .show();   
                           }else{
                              finish();
                           }
                        return true;
                     }
               }
            return super.onKeyDown(keyCode,event);
         }
      
      @Override
      public void onDestroy(){
            super.onDestroy();
            clearAll();
         }
      private void saveFile(){
            ShowDialog.showLogin(ZipView.this,"保存");
            new Thread(new Runnable(){
                     public void run(){
                           String out=zipFile.getName();
                           int i=out.lastIndexOf(".");
                           if(i != -1){
                                 out=out.substring(0,i)+(isSigne?".signed":".new")+out.substring(i);
                              }
                           File file=new File(out);
                           try
                              {
                                 zip ( zipFile, zipEnties, file );
                              }
                           catch (IOException e)
                              {}
                           o.sendEmptyMessage(1);
                        }
                  }).start();
         }
         Handler o=new Handler()
            {

               @Override
               public void handleMessage ( Message msg )
                  {
                     super.handleMessage ( msg );
                     com.zhl.cbdialog.CBDialogBuilder.dialog.dismiss ( );
                    
                     finish();
                  }
            
         };
      public void clearAll(){
            zipEnties=null;
            zipFile=null;
            path=null;
            dep=0;
            file=null;
            System.gc();
         }
      private void removeFile(String name){
            zipEnties.remove(tree.getCurPath()+name);
         }
      private void removeDirectory(String name){
            Map<String,byte[]> zipEnties=this.zipEnties;
            Tree tree=this.tree;
            String curr=tree.getCurPath();
            Set<String> keySet=zipEnties.keySet();
            String[] keys=new String[keySet.size()];
            keySet.toArray(keys);
            for(String key:keys){
                  if(key.startsWith(curr+name)){
                        zipEnties.remove(key);
                     }
               }
         }
      private byte[] readEntry(String name){
            byte[] buf=zipEnties.get(tree.getCurPath()+name);
            if(buf == null){
                  return readEntryForZip(tree.getCurPath()+name); 
               }
            return buf;
         }



      private String extract(String name)throws Exception{
            String str=zipFile.getName();
            int s=str.lastIndexOf('/');
            int e=str.indexOf('.');
            if(s<e)
               str=str.substring(s,e);
            File outPath=new File(EXTRACTPATH+str);
            Map<String,byte[]> zipEnties=this.zipEnties;
            String curr=tree.getCurPath();
            curr=tree.isDirectory(name)?curr+name+"/":curr+name;
            List<String> extractFiles=new ArrayList<String>();
            for(String key:zipEnties.keySet()){
                  if(key.startsWith(curr)){
                        byte[] buf=zipEnties.get(key);
                        if(buf != null){
                              ZipExtract.extractEntryForByteArray(buf,key,outPath);
                           }else{
                              ZipEntry entry=zipFile.getEntry(key);
                              ZipExtract.extractEntry(zipFile,entry,outPath);
                           }
                     }
               }
               return outPath.getAbsolutePath();
         }



      private byte[] readEntryAbsName(String name){
            byte[] buf=zipEnties.get(name);
            if(buf == null){
                  return readEntryForZip(name); 
               }
            return buf;
         }




      private ZipEntry getEntry(String name){
            byte[] buf=zipEnties.get(tree.getCurPath()+name);
            if(buf == null){
                  if(zipFile!=null)
                     return zipFile.getEntry(tree.getCurPath()+name);
                  ZipEntry zipEntry=new ZipEntry(tree.getCurPath()+name);
                  zipEntry.setTime(0);
                  zipEntry.setSize(0);
                  return zipEntry;

               }
            ZipEntry zipEntry=new ZipEntry(tree.getCurPath()+name);
            zipEntry.setTime(System.currentTimeMillis());
            zipEntry.setSize(buf.length);
            return zipEntry;
         }



      private byte[] readEntryForZip(String name){
            ZipEntry zipEntry=zipFile.getEntry(name);
            if(zipEntry != null){
                  ByteArrayOutputStream baos= new ByteArrayOutputStream(8*1024);
                  byte[] buf=new byte[4*1024];
                  try{
                        InputStream in=zipFile.getInputStream(zipEntry);
                        int count;
                        while((count=in.read(buf, 0, buf.length)) !=-1){
                              baos.write(buf,0,count);
                           }
                        in.close();
                        baos.close();
                     }catch(IOException io){}

                  return baos.toByteArray();
               }
            return null;
         }




      private class FileListAdapter extends BaseAdapter {
            protected final Context mContext;
            protected final LayoutInflater mInflater;
            public FileListAdapter(Context context) {
                  mContext = context;
                  mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               }
            public int getCount() {
                  return fileList.size();
               }
            public Object getItem(int position) {
                  return fileList.get(position);
               }
            public long getItemId(int position) {
                  return position;
               }
            public View getView(int position, View convertView, ViewGroup parent) {
                String file=fileList.get(position);
                 RelativeLayout container = null;
                  if(convertView==null){
                        container = (RelativeLayout) mInflater.inflate(R.layout.zip_list_item, null);
                     }else{
                        container=(RelativeLayout)convertView;
                     }
                  ImageView icon = (ImageView) container.findViewById(R.id.icon);
                  int resourceId;
                  if (tree.isDirectory(file)) {
                        resourceId = R.drawable.ic_directory;
                     } else {
                        resourceId = R.drawable.ic_directory;
                     }
                  icon.setImageResource(resourceId);
                  if(file.endsWith(".jpg")
                     || file.endsWith(".png")
                     || file.endsWith(".gif")
                     || file.endsWith(".jpeg")
                     || file.endsWith(".bmp"))
                     {
                        Glide.with(App.thzz).load(readEntry(file)).crossFade().into(icon);
                     }                 
                  TextView text = (TextView) container.findViewById(R.id.text);
                  TextView perm = (TextView) container.findViewById(R.id.permissions);
                  TextView time = (TextView) container.findViewById(R.id.times);
                  TextView size = (TextView) container.findViewById(R.id.size);
                  text.setText(file);
                  perm.setText("");
                  if(!tree.isDirectory(file)){
                        ZipEntry zipEntry=getEntry(file);
                        Date date=new Date(zipEntry.getTime());
                        SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                        time.setText(format.format(date));
                        size.setText(convertBytesLength(zipEntry.getSize()));
                     }else{
                        time.setText("");
                        size.setText("");
                     }
                  return container;
               }

            private String convertBytesLength(long len){
                  if(len<1024){
                        return len+"B";
                     }
                  if(len<1024*1024){
                        return String.format("%.2f%s",(len/1024.0),"K");
                     }
                  if(len<1024*1024*1024)
                     return String.format("%.2f%s",(len/(1024*1024.0)),"M");
                  return String.format("%.2f%s",(len/(1024*1024*1024.0)),"G");
               }


         }
      static class Tree{
            List<Map<String,String>> node;
            Comparator<String> sortByType=new Comparator<String>(){
                  public int compare(String a,String b){
                        if(isDirectory(a) && !isDirectory(b)){
                              return -1;
                           }
                        if(!isDirectory(a) &&isDirectory(b)){
                              return 1;
                           }
                        return a.toLowerCase().compareTo(b.toLowerCase());
                     }
               };

            public Tree(Set<String> names){
                  if(path==null){
                        path=new Stack<String>();
                        dep=0;
                     }
                  HashMap<String,byte[]> zipEnties=ZipView.zipEnties;
                  node=new ArrayList<Map<String,String>>();
                  for(String name :names){
                        String[] token=name.split("/");
                        String tmp="";
                        for(int i=0,len=token.length;i<len;i++){
                              String value=token[i];
                              if(i>=node.size()){
                                    Map<String,String> map=new HashMap<String,String>();
                                    if(zipEnties.containsKey(tmp+value)
                                       &&i+1 == len){
                                          map.put(tmp+value,tmp);
                                       }else{
                                          map.put(tmp+value+"/",tmp);
                                       }
                                    node.add(map);
                                    tmp+=value+"/";
                                 }else{
                                    Map<String,String> map=node.get(i);
                                    if(zipEnties.containsKey(tmp+value)
                                       &&i+1 == len){
                                          map.put(tmp+value,tmp);
                                       }else{
                                          map.put(tmp+value+"/",tmp);
                                       }
                                    tmp+=value+"/";
                                 }
                           }
                     }
               }




            private List<String> list(String parent){
                  Map<String,String> map=null;
                  List<String> str=new ArrayList<String>();
                  while(dep>=0&&node.size()>0){
                        map=node.get(dep);
                        if(map != null){
                              break;
                           }
                        pop();
                     }
                  if(map ==null){
                        return str;
                     }
                  for(String key :map.keySet()){
                        if(parent.equals(map.get(key))){
                              int index;
                              if(key.endsWith("/")){
                                    index=key.lastIndexOf("/",key.length()-2);
                                 }else{
                                    index=key.lastIndexOf("/");
                                 }
                              if(index != -1)
                                 key=key.substring(index+1);
                              str.add(key);
                              //        Log.e("tree",key);
                           }
                     }
                  Collections.sort(str,sortByType);

                  return str;
               }

            public void addNode(String name){
                  Map<String,String> map=node.get(dep);
                  map.put(getCurPath()+name,getCurPath());
               }

            public void deleteNode(String name){
                  Map<String,String> map=node.get(dep);
                  map.remove(getCurPath()+name);
               }

            public List<String> list(){
                  return list(getCurPath());
               }
            public void push(String name){
                  dep++;
                  path.push(name);
               }
            public String pop(){
                  if(dep>0){
                        dep--;
                        return path.pop();
                     }
                  return null;
               }
            public String getCurPath(){

                  //    Log.e("tree Curpath",join(path,"/"));
                  return join(path,"/");
               }
            public boolean isDirectory(String name){
                  return name.endsWith("/");
               }

            private String join(Stack<String> stack,String d){
                  StringBuilder sb=new StringBuilder("");
                  for(String s: stack){
                        sb.append(s);
                     }
                  return sb.toString();
               }


         }


      private static void readZip(ZipFile zip,Map<String,byte[]> map)throws IOException{
            Enumeration enums=zip.entries();
            while(enums.hasMoreElements()){
                  ZipEntry entry=(ZipEntry)enums.nextElement();
                  if(!entry.isDirectory()){
                        map.put(entry.getName(), null);
                     }
               }

         }


      private static byte[] readEntry(ZipFile zipFile,String name)throws IOException{
            ZipEntry zipEntry=zipFile.getEntry(name);
            if(zipEntry != null){
                  ByteArrayOutputStream baos = new ByteArrayOutputStream(8*1024);
                  InputStream in=zipFile.getInputStream(zipEntry);
                  byte[] buf=new byte[4*1024];
                  int count;
                  while((count=in.read(buf, 0, buf.length)) !=-1){
                        baos.write(buf,0,count);
                     }
                  return baos.toByteArray();
               }
            return null;
         }


      public static void zip(ZipFile zipFile,Map<String,byte[]> map,File file)throws IOException {
            FileOutputStream out = new FileOutputStream(file);
            ZipOutputStream zos =new ZipOutputStream(out);
            byte[] buf=new byte[10*1024];

            for( String key : map.keySet()) {
                  byte[] data=map.get(key);
                  if(data != null){

                        ZipEntry zipEntry=new ZipEntry(key);
                        zipEntry.setSize(data.length);
                        zipEntry.setTime(System.currentTimeMillis());
                        zos.putNextEntry(zipEntry);
                        zos.write(data);
                     }            
                  else{
                        ZipEntry zipEntry=zipFile.getEntry(key);
                        if(zipEntry != null){
                              InputStream in=zipFile.getInputStream(zipEntry);
                              zos.putNextEntry(zipEntry);
                              int count;
                              while((count=in.read(buf, 0, buf.length)) !=-1)
                                 zos.write(buf,0,count);
                           }
                     }
                  zos.flush();
               }
            zos.close();
         }
   }

