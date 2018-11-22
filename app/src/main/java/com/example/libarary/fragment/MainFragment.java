package com.example.libarary.fragment;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.libarary.R;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    private FragmentManager manager;
    private AnnouncementFragment announcementFragment = new AnnouncementFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private ResourcesFragment resourcesFragment = new ResourcesFragment();
    private ServiceFragment serviceFragment = new ServiceFragment();
    private RadioButton anno,chat,reso,serv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getActivity().getSupportFragmentManager();
        announcementFragment = new AnnouncementFragment();
        chatFragment = new ChatFragment();
        resourcesFragment = new ResourcesFragment();
        serviceFragment = new ServiceFragment();
        manager.beginTransaction().add(R.id.content,announcementFragment)
                .add(R.id.content,chatFragment)
                .add(R.id.content,resourcesFragment)
                .add(R.id.content,serviceFragment).commit();
        manager.beginTransaction().hide(chatFragment)
                .hide(resourcesFragment)
                .hide(serviceFragment).commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anno = (RadioButton) view.findViewById(R.id.gonggao);
        anno.setOnClickListener(this);
        chat = (RadioButton) view.findViewById(R.id.jiaoliu);
        chat.setOnClickListener(this);
        reso = (RadioButton) view.findViewById(R.id.ziyuan);
        reso.setOnClickListener(this);
        serv = (RadioButton) view.findViewById(R.id.fuwu);
        serv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gonggao:
                manager.beginTransaction()
                    .hide(resourcesFragment)
                    .hide(serviceFragment)
                    .hide(chatFragment)
                    .show(announcementFragment).commit();
                anno.setChecked(true);
                break;
            case R.id.jiaoliu:
                manager.beginTransaction()
                        .hide(resourcesFragment)
                        .hide(serviceFragment)
                        .hide(announcementFragment)
                        .show(chatFragment).commit();
                chat.setChecked(true);
                break;
            case R.id.ziyuan:
                manager.beginTransaction()
                        .hide(announcementFragment)
                        .hide(serviceFragment)
                        .hide(chatFragment)
                        .show(resourcesFragment).commit();
                reso.setChecked(true);
                break;
            case R.id.fuwu:
                manager.beginTransaction()
                        .hide(resourcesFragment)
                        .hide(announcementFragment)
                        .hide(chatFragment)
                        .show(serviceFragment).commit();
                serv.setChecked(true);
                break;
        }
    }
}
