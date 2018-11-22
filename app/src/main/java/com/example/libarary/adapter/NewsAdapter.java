package com.example.libarary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.view.DiscussActivity;
import com.example.libarary.xinxibao.Comment;
import com.example.libarary.xinxibao.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 陈金桁 on 2018/1/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Post> mPost;
    private Context context;
//    private List<Comment> mComment = new ArrayList<>();

    public NewsAdapter(Context context,List<Post> mPost){
        this.mPost = mPost;
        this.context = context;
        Log.e("NewsAdapter",mPost.size() + "");
//        this.mComment = mComment;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = mPost.get(position);
//        Comment comment = mComment.get(position);
        Intent intent1 = new Intent();
        intent1.putExtra("user4",post.getObjectId());
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());
        holder.person.setText(post.getName());
        holder.phone.setText(post.getPhone());
        holder.discuss.setEnabled(true);
        holder.show.setEnabled(true);



        holder.discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.discuss.setEnabled(false);
                Log.e("adapetr","discuss");
                Intent intent = new Intent(context,DiscussActivity.class);
                intent.putExtra("user",post.getTitle());
                intent.putExtra("user1",post.getContent());
                intent.putExtra("user2",post.getObjectId());
                Log.e("aaa",post.getObjectId());
                context.startActivity(intent);
                holder.discuss.setEnabled(true);
            }
        });

        final List<Comment> mComment = new ArrayList<Comment>();
        Log.e("aaaa",post.getObjectId() + "");
        BmobQuery<Comment> query = new BmobQuery<>();
        query.include("post,user");
        post.setObjectId(post.getObjectId());
        query.addWhereEqualTo("post",new BmobPointer(post));

        query.setSkip(mComment.size());
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null){

                    for(Comment comment : list){
                        Log.e("comment2",comment.getContent());
                        Log.e("comment2",comment.getUser().getUsername());
                        mComment.add(0,comment);
                        Log.e("mComment",mComment.get(0).getContent());
                    }
                    holder.recyclerView.getAdapter().notifyDataSetChanged();

                }
                else{
                    Log.e("tga",e.toString());
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        DisAdapter disAdapter = new DisAdapter(context, mComment, mPost);
        holder.recyclerView.setAdapter(disAdapter);
//        holder.show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final List<Comment> mComment = new ArrayList<Comment>();
//                Log.e("aaaa",post.getObjectId() + "");
//                BmobQuery<Comment> query = new BmobQuery<>();
//                query.include("post,user");
//                post.setObjectId(post.getObjectId());
//                query.addWhereEqualTo("post",new BmobPointer(post));
//
//                query.setSkip(mComment.size());
//                query.findObjects(new FindListener<Comment>() {
//                    @Override
//                    public void done(List<Comment> list, BmobException e) {
//                        if(e == null){
//
//                            for(Comment comment : list){
//                                Log.e("comment2",comment.getContent());
//                                Log.e("comment2",comment.getUser().getUsername());
//                                mComment.add(0,comment);
//                                Log.e("mComment",mComment.get(0).getContent());
//                        }
//                    holder.recyclerView.getAdapter().notifyDataSetChanged();
//
//                }
//                else{
//                    Log.e("tga",e.toString());
//                }
//            }
//        });
//                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//                holder.recyclerView.setLayoutManager(layoutManager);
//                DisAdapter disAdapter = new DisAdapter(context, mComment, mPost);
//                holder.recyclerView.setAdapter(disAdapter);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,content,person,phone,content1,person1,phone1;
        ImageButton discuss,show;
        RecyclerView recyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            person = (TextView) itemView.findViewById(R.id.people);
            phone = (TextView) itemView.findViewById(R.id.phone);
            discuss = (ImageButton) itemView.findViewById(R.id.discuss);
            show = (ImageButton) itemView.findViewById(R.id.show);
//            content1 = (TextView) itemView.findViewById(R.id.content1);
//            person1 = (TextView) itemView.findViewById(R.id.people1);
//            phone1 = (TextView) itemView.findViewById(R.id.phone1);
        }
    }
}
