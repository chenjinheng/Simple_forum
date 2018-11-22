package com.example.libarary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libarary.R;
import com.example.libarary.view.MapActivity;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class ResourcesFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resource,container,false);
        startActivity(new Intent(getActivity(),MapActivity.class));
        return view;
    }
}
