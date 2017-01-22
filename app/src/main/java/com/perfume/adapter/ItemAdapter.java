package com.perfume.adapter;

import android.view.*;
import android.widget.*;

import android.support.v7.widget.RecyclerView;
import android.view.View.OnClickListener;
import com.perfume.R;
import com.perfume.VideoPlay.ItemEntity;
import com.perfume.adapter.ItemAdapter;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder>
 {
      private ArrayList<ItemEntity> items;
      
      public static interface OnRecyclerViewItemClickListener {
            void onItemClick(View view,int i);
         }
      private OnRecyclerViewItemClickListener mOnItemClickListener = null;
      public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            mOnItemClickListener = listener;
         }
      public ItemAdapter(ArrayList<ItemEntity> items) {
            this.items = items;
         }
      @Override
      public viewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card,
                                                                            viewGroup, false);
            return new viewHolder(view);
         }

      @Override
      public int getItemViewType(int position) {
            return super.getItemViewType(position);
         }

      @Override
      public void onBindViewHolder(viewHolder viewHolder, final int position) {
            String info = items.get(position).getName();
            View view = viewHolder.itemView;
            TextView textView = (TextView) view.findViewById(R.id.info_text);
            textView.setText(info);
            ImageView src=(ImageView) view.findViewById(R.id.src);
            LinearLayout layout=(LinearLayout) view.findViewById(R.id.layout);
            layout.setOnClickListener ( new OnClickListener ( ){

                     @Override
                     public void onClick ( View p1 )
                        {
                           mOnItemClickListener.onItemClick(p1,position);
                        }
                  } );
         }

      @Override
      public int getItemCount() {
            return items.size();
         }

      class viewHolder extends RecyclerView.ViewHolder {
            public viewHolder(View itemView) {
                  super(itemView);
               }
         }
   }
