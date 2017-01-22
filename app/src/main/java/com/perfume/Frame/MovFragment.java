package com.perfume.Frame;

import android.support.v7.widget.*;
import android.view.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.perfume.R;
import com.perfume.Utis.SnackbarShow;
import com.perfume.VideoPlay.ItemEntity;
import com.perfume.adapter.ItemAdapter;
import java.util.ArrayList;

public class MovFragment extends Fragment
   {
      private RecyclerView mRecyclerView;
      private ItemAdapter mAdapter;
      private RecyclerView.LayoutManager mLayoutManager;
      private ArrayList<ItemEntity> items = new ArrayList<> ( );
      private View mView;
      @Override
      public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
         {
            if(mView==null)
               {
                  mView = inflater.inflate ( R.layout.activity_kaifa, container, false );
                  initView();
               } 
            return mView;
         }
      private void initView ( )
         {
            mRecyclerView = (RecyclerView)mView. findViewById ( R.id.recyclerView );
            int spanCount = 2;
            mLayoutManager = new StaggeredGridLayoutManager (spanCount,  StaggeredGridLayoutManager.VERTICAL );
            mRecyclerView.setLayoutManager ( mLayoutManager );
            for ( int i = 0; i < 10; i++ )
               {
                  ItemEntity list=new ItemEntity();
                  list.setName("插件使用第" +(i+1)+"课");
                  items.add ( list );
               }
            mAdapter = new ItemAdapter ( items );
            mRecyclerView.setAdapter ( mAdapter );
            mAdapter.setOnItemClickListener ( new ItemAdapter. OnRecyclerViewItemClickListener ( ){

                     @Override
                     public void onItemClick ( View view,int i )
                        {
                           SnackbarShow A=new SnackbarShow ( );
                           A.LongShow ( view, items.get(i).getName(), "Yes" );
                           A.setOnItemClickListener ( new  SnackbarShow.OnTextClick ( ){
                                    @Override
                                    public void OnSnackbarClick ( View v )
                                       {
                                          
                                       }
                                 } );
                        }
                  } );
         }
   }
