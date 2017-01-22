package com.perfume.adapter;

import android.view.*;
import android.widget.*;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.perfume.R;
import com.perfume.VideoPlay.ItemEntity;
import java.util.ArrayList;

/*
电影item
*/
public class ItemList extends BaseAdapter
 {

      private Context context;
      private ArrayList<ItemEntity> list;

      public ItemList(Context context, ArrayList<ItemEntity> list) {
            this.context = context;
            this.list = list;
         }

      public void onDateChange(ArrayList<ItemEntity> list) {
            this.list = list;
            this.notifyDataSetChanged();
         }

      @Override
      public int getCount() {
            return list.size();
         }

      @Override
      public Object getItem(int position) {
            return list.get(position);
         }

      @Override
      public long getItemId(int position) {
            return position;
         }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                  holder = new ViewHolder();
                  convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                  holder.mImageView = (ImageView) convertView.findViewById(R.id.avatar);
                  holder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
                  convertView.setTag(holder);
               } else {
                  holder = ((ViewHolder) convertView.getTag());
               }
            holder.mName = list.get(position).getName();
            holder.mImage = list.get(position).getTb();
            holder.mMsg = list.get(position).getMsg();
            holder.mUri = list.get(position).getUrl();
            holder.mPingfen = list.get(position).getPingfen();
            holder.mYuanyuan = list.get(position).getYanyuan();
            holder.mTextView.setText(list.get(position).getName());
            Glide.with(holder.mImageView.getContext()).load(list.get(position).getTb()).fitCenter().into(holder.mImageView);
            return convertView;
         }

      class ViewHolder {
            public String mName;
            public String mUri;
            public String mImage;
            public String mPingfen;
            public String mMsg;
            public String mYuanyuan;
            public  ImageView mImageView;
            public  TextView mTextView;
         }
   }


