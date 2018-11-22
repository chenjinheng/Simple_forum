package com.example.libarary.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.adapter.AddAdapter;
import com.example.libarary.xinxibao.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private AddAdapter addAdapter;
    private List<User> users;
    private BmobQuery<User> bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bmobUser.addWhereEqualTo("username",searchView.getQuery().toString());
                bmobUser.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e == null){
                            if(list.size() == 0){
                                Toast.makeText(AddActivity.this, "没找到对应用户", Toast.LENGTH_SHORT).show();
                                return ;
                            }
                            Toast.makeText(AddActivity.this, "找到用户", Toast.LENGTH_SHORT).show();
                            users.clear();
                            users.addAll(list);
                            addAdapter.notifyDataSetChanged();
                        }
                        else{
                            Logger.e(e.getMessage());
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_finduser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        users = new ArrayList<>();
        addAdapter = new AddAdapter(AddActivity.this,users);
        recyclerView.setAdapter(addAdapter);
        bmobUser = new BmobQuery<>();
        searchView = (SearchView) findViewById(R.id.search_view);
    }
}
