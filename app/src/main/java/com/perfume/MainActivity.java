package com.perfume;

import android.os.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.view.*;
import com.perfume.Frame.*;
import com.perfume.Utis.*;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import com.perfume.FileAdapter.DirectoryFragment;
import com.perfume.UI.FabOnClick;
import com.perfume.VideoPlay.MovieList;
import com.perfume.activity.ActivityAbout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
主窗口
*/

public class MainActivity extends AppCompatActivity implements DirectoryFragment.DocumentSelectActivityDelegate
,NavigationView.OnNavigationItemSelectedListener    
   {

      @Override
      public boolean onNavigationItemSelected ( MenuItem p1 )
         {
            int id=p1.getItemId ( );
            switch ( id )
               {
                  case R.id.nav_about:
                     /*
                     关于
                     */
                     StartAbout ( );
                     break;
                  case R.id.nav_apktool:
                     /*
                     反编译
                     */
                     i=2;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, a).commit ( );
                     break;
                  case R.id.nav_mov:
                     /*
                     在线电影
                     */
                     i=1;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, mov ).commit ( );
                     break;
                  case R.id.nav_user:
                     /*
                     用户信息
                     */
                     i=1;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, Login ).commit();
                     break;
                  case R.id.nav_aide:
                     /*
                     开发视频
                     */
                     i=1;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, kaifa ).commit();
                     break;
                  case R.id.nav_voide:
                     /*
                      开发视频
                      */
                     i=1;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, movjiaoxue ).commit();
                     break;
                  case R.id.nav_xp:
                     /*
                      开发视频
                      */
                     i=1;
                     getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, Screen ).commit();
                     break;
               }
            drawer.closeDrawer ( GravityCompat.START );
            toolbar.setTitle ( p1.getTitle ( ).toString ( ) );
            SimpleDateFormat formatter = new SimpleDateFormat("当前时间: yyyy-MM-dd HH:mm:ss");
            String data = formatter.format(System.currentTimeMillis());
            toolbar.setSubtitle(data);
            return true;
         }

      @Override
      public void didSelectFiles ( DirectoryFragment activity, ArrayList<String> files )
         {
         }
      @Override
      public void startDocumentSelectActivity ( )
         {
         }
      @Override
      public void updateToolBarName ( String name )
         {
            toolbar.setSubtitle ( name );
         }
      private Toolbar toolbar;
      private DrawerLayout drawer;
      private FloatingActionButton fab;
      private View ViewHead;
      private DirectoryFragment a;
      private MovieList mov;
      private LoginFragment Login;
      private KaiFaFragment kaifa;
      private MovFragment movjiaoxue;
      private ScreenFragment Screen;
      private int i=0;
      public static boolean isLogin=true;
      private PermissionHelper mPermissionHelper;
      @Override
      protected void onCreate ( Bundle savedInstanceState )
         {
            super.onCreate ( savedInstanceState );
            setContentView ( R.layout.activity_main );
            toolbar = (Toolbar) findViewById ( R.id.toolbar );
            setSupportActionBar ( toolbar );
            drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle ( this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
            drawer.setDrawerListener ( toggle );
            toggle.syncState ( );
            NavigationView navigationView = (NavigationView) findViewById ( R.id.nav_view );
            ViewHead = navigationView.inflateHeaderView ( R.layout.nav_header_main );
            navigationView.setNavigationItemSelectedListener ( this );
            fab = (FloatingActionButton) findViewById ( R.id.fab );
            fab.setOnClickListener ( new FabOnClick ( ) );
            a = new DirectoryFragment ( );
            mov=new MovieList();
            Login=new LoginFragment();
            kaifa=new KaiFaFragment();
            movjiaoxue=new MovFragment();
            Screen=new ScreenFragment();
            a.setDelegate ( this );
            mPermissionHelper = new PermissionHelper ( this );
            mPermissionHelper.setOnApplyPermissionListener ( new PermissionHelper.OnApplyPermissionListener ( ) {
                     @Override
                     public void onAfterApplyAllPermission ( )
                        {
                           runApp ( );
                        }
                  } );
            if ( Build.VERSION.SDK_INT < 23 )
               {
                  runApp ( );
               }
            else
               {
                  if ( mPermissionHelper.isAllRequestedPermissionGranted ( ) )
                     {
                        runApp ( );
                     }
                  else
                     {
                        mPermissionHelper.applyPermissions ( );
                     }
               }
         }

      @Override
      public void onRequestPermissionsResult ( int requestCode, String[] permissions, int[] grantResults )
         {
            super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
            mPermissionHelper.onRequestPermissionsResult ( requestCode, permissions, grantResults );
         }
      @Override
      protected void onActivityResult ( int requestCode, int resultCode, Intent data )
         {
            super.onActivityResult ( requestCode, resultCode, data );
            mPermissionHelper.onActivityResult ( requestCode, resultCode, data );
         }
      private void runApp ( )
         {
            getSupportFragmentManager ( ).beginTransaction ( ).replace ( R.id.Frame, a ).commit ( );
          //  StartAD.ShowChapi ( this );
//            StartAD.ShowMini(this);
//            StartAD.ShowBannerBottom(this);
//            StartYouMiAD.ShowSpot(this);
//            StartSlideableSpotAd.ShowSpot(this);
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
//            final View bannerView = BannerManager.getInstance(this).getBannerView(this, new BannerViewListener() {
//                     @Override
//                     public void onRequestSuccess() {
//                        }
//                     @Override
//                     public void onSwitchBanner() {
//                        }
//                     @Override
//                     public void onRequestFailed() {
//                        }
//                  });
//         ((Activity) this).addContentView(bannerView, layoutParams);
//            new Handler().postDelayed(new Runnable(){   
//                     public void run() {   
//                           SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                           String data = formatter.format(TimeUtis.getCurTimeMills()); 
//                           Time.setText(data);
//                        }   
//                  }, 1000);   
         }

      @Override
      public void onBackPressed ( )
         {

            if ( i == 0 || i == 2 )
               {
                  if ( a.onBackPressed_ ( ) )
                     {
                        if ( drawer.isDrawerOpen ( GravityCompat.START ) )
                           {
                              drawer.closeDrawer ( GravityCompat.START );
                           }
                        else
                           {
                              SnackbarShow A=new SnackbarShow ( );
                              A.LongShow ( fab, "Close App", "Yes" );
                              A.setOnItemClickListener ( new  SnackbarShow.OnTextClick ( ){
                                       @Override
                                       public void OnSnackbarClick ( View v )
                                          {
                                             finish ( );
                                          }
                                    } );
                           }
                     }
               }
               else
               {
                  SnackbarShow A=new SnackbarShow ( );
                  A.LongShow ( fab, "Close App", "Yes" );
                  A.setOnItemClickListener ( new  SnackbarShow.OnTextClick ( ){
                           @Override
                           public void OnSnackbarClick ( View v )
                              {
                                 finish ( );
                              }
                        } );
               }
         }
      @Override
      public boolean onCreateOptionsMenu ( Menu menu )
         {
            menu.add ( 0, 0, 0, "加入官方群" ).setShowAsAction ( 1 );
            return super.onCreateOptionsMenu ( menu );
         }
      @Override
      public boolean onOptionsItemSelected ( MenuItem item )
         {
            switch ( item.getItemId ( ) )
               {
                  case 0:
                     if ( !Tencent.IsInstallQ ( this, "com.tencent.mobileqq" ) )
                        {
                           ToastShow.ShortShow ( this, "您未安装QQ！" );
                           break;
                        }
                     Tencent.StartQ ( MainActivity.this, "DQQRVE6jjxdxucwBxLQmjeH2q6WHHVLT" );
                     break;
               }
            return super.onOptionsItemSelected ( item );
         }
      public  void StartAbout ( )
         {
            startActivity ( new Intent ( MainActivity.this, ActivityAbout.class ) );
         }
   }
