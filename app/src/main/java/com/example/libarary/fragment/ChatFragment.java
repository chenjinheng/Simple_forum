package com.example.libarary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.libarary.R;
import com.example.libarary.user.FindActivity;
import com.example.libarary.view.AddActivity;
import com.example.libarary.view.AskActivity;
import com.example.libarary.view.ChangeActivity;
import com.example.libarary.view.FriendActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class ChatFragment  extends Fragment{
    private SimpleDraweeView simpleDraweeView;
    private Button head,end,friend;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatfragment,container,false);
        simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_logo1);
        simpleDraweeView.setImageURI("res://com.example.libarary/" + R.drawable.touxiang);
        init(view);
        initButton();
        return view;
    }
    public void init(View view){
        head = (Button) view.findViewById(R.id.change_head);
        end = (Button) view.findViewById(R.id.end);
        friend = (Button) view.findViewById(R.id.friend);

    }
    public void initButton(){
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangeActivity.class));
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobIM.getInstance().disConnect();
                BmobUser.logOut();
                getActivity().finish();
            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FriendActivity.class));
            }
        });

    }
}
