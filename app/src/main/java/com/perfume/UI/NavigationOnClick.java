package com.perfume.UI;
import com.perfume.*;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import com.perfume.Utis.ToastShow;
import android.content.Intent;
import com.perfume.activity.ActivityAbout;

public class NavigationOnClick extends MainActivity implements NavigationView.OnNavigationItemSelectedListener    
   {

      @Override
      public boolean onNavigationItemSelected ( MenuItem p1 )
         {
            int id=p1.getItemId ( );
            switch ( id )
               {
                  case R.id.nav_about:
                     StartAbout();
                     break;
               }
          //  MainActivity.drawer.closeDrawer(GravityCompat.START);
            return true;
         }
   }
