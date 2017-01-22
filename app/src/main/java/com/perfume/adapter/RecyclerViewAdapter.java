package com.perfume.adapter;



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.perfume.R;


/**
 * Created by finch on 2016/7/16.
 */
public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

      private List<String> datas;

      public static interface OnRecyclerViewItemClickListener {
            void onItemClick(View view);
         }
      private OnRecyclerViewItemClickListener mOnItemClickListener = null;
      public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            mOnItemClickListener = listener;
         }
      public RecyclerViewAdapter(List<String> datas) {
            this.datas=datas;
         }

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
         {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
            v.setOnClickListener(new View.OnClickListener(){
                     @Override
                     public void onClick(View v) {
                           mOnItemClickListener.onItemClick(v);
                        }
                  });
            ViewHolder vh = new ViewHolder(v);
            return vh;
         }

      @Override
      public void onBindViewHolder(ViewHolder holder, int position) {
            holder.item.setTag(position);
            holder.tv.setText(datas.get(position));
         }

      class ViewHolder extends RecyclerView.ViewHolder
         {
            public View item;
            public TextView tv;
            public ViewHolder(View view){
                  super(view);
                  item = view;
                  tv = (TextView) view.findViewById(R.id.text);
               }
         }
      @Override
      public int getItemCount()
         {
            return datas.size();
         }

      public void addItem(String s, int position) {
            datas.add(position, s);
            notifyItemInserted(position);
         }

      public void removeItem(final int position) {
            datas.remove(position);
            notifyItemRemoved(position);
         }
   }

