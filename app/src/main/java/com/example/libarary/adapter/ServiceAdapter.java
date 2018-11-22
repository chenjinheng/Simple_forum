package com.example.libarary.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.xinxibao.infBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 陈金桁 on 2018/2/7.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<infBean> mList = new ArrayList<>();
    public ServiceAdapter(Context context){
        mLayoutInflater = LayoutInflater.from(context);
    }
    public void setList(List<infBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    public void appendList(ArrayList<infBean> lists){
        int lastPosition = mList.size() - 1;
        mList.addAll(lists);
        notifyItemRangeInserted(lastPosition + 1,lists.size());
    }
    public void removeList(int size){
        int num = size;
        while(num > 0){
            mList.remove(mList.size() - 1);
            num--;
        }
        notifyItemRangeRemoved(mList.size(),size);
    }
    public int getListSize(){
        return mList.size();
    }

//    public static class Item<T>{
//        int type;
//        T data;
//
//        public Item(int type, T data) {
//            this.type = type;
//            this.data = data;
//        }
//    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.service_adapter,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uri uri = Uri.parse("res://com.example.libarary/" + mList.get(position).getImgId());
        holder.sbj.setText(mList.get(position).getSbj());
        holder.iv_sbj.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return getListSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_sbj)
        TextView sbj;
        SimpleDraweeView iv_sbj;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            iv_sbj = (SimpleDraweeView) itemView.findViewById(R.id.iv_sbj);
        }
    }
}
