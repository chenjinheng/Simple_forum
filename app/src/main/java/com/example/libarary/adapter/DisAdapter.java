package com.example.libarary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libarary.R;
import com.example.libarary.xinxibao.Comment;
import com.example.libarary.xinxibao.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈金桁 on 2018/1/18.
 */

public class DisAdapter extends RecyclerView.Adapter<DisAdapter.ViewHolder> {
    private Context context;
    private List<Comment> mComment;
    private List<Post> mPost;
    public DisAdapter(Context context, List<Comment> mComment, List<Post> mPost) {
        this.context = context;
        this.mComment = mComment;
        this.mPost = mPost;
//        Log.e("dis",mComment.get(0).getContent());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discuss,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comment comment = mComment.get(position);
//        Post post = mPost.get(position);

                        holder.content.setText(comment.getContent());
            holder.people.setText(comment.getUser().getUsername());




//        if(comment.getPost().getObjectId().equals(post.getObjectId())) {
//            holder.content.setText(comment.getContent());
//            holder.people.setText(comment.getUser().getUsername());
//        }
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView content,people;
        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.context);
            people = (TextView) itemView.findViewById(R.id.people);
        }
    }
}
