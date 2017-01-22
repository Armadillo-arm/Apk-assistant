package com.perfume.Frame;
import android.view.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.perfume.R;
/*
用户信息
*/
public class LoginFragment extends Fragment
   {
      private View mView;
      @Override
      public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
         {
            if(mView==null)
            {
               mView=inflater.inflate(R.layout.activity_login,container,false);
            }
            return mView;
         }
}
