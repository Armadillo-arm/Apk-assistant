package com.perfume.Frame;
import android.widget.FrameLayout;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import com.perfume.R;

public class HomeFragment extends Fragment
   {
      private View mView;
      @Override
      public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
         {
            mView=inflater.inflate(R.layout.frame_home,container,false);
            return mView;
         }
}
