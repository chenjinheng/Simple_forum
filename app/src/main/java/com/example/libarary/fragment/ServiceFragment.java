package com.example.libarary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libarary.R;
import com.example.libarary.adapter.ServiceAdapter;
import com.example.libarary.xinxibao.infBean;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class ServiceFragment extends Fragment {
    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private List<infBean> datas;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.servicefragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.service_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        datas = Arrays.asList(
            new infBean(R.drawable.yuyue,"预约区"),
            new infBean(R.drawable.chaxun,"查询区"),
            new infBean(R.drawable.dayi,"答疑区"),
            new infBean(R.drawable.kezhuo,"座位区"),
            new infBean(R.drawable.kebiao,"课表区"),
            new infBean(R.drawable.yijian,"意见区")
        );
        serviceAdapter = new ServiceAdapter(getActivity());
        serviceAdapter.setList(datas);
        recyclerView.setAdapter(serviceAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
