package com.example.libarary.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.libarary.R;
import com.example.libarary.adapter.DisAdapter;
import com.example.libarary.adapter.NewsAdapter;
import com.example.libarary.view.BannerActivity;
import com.example.libarary.view.PostActivity;
import com.example.libarary.xinxibao.Comment;
import com.example.libarary.xinxibao.Post;
import com.example.libarary.xinxibao.User;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 陈金桁 on 2018/1/14.
 */

public class AnnouncementFragment extends Fragment {
    private User user;
    private Banner banner;
    private SwipeRefreshLayout swipe;
    private List<Integer> bannerImage;
    private List<String> list_path;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<Post> posts = new ArrayList<>();
//    private List<Comment> mComment = new ArrayList<>();
    private ArrayList<String> list_title = new ArrayList<>();
    private Boolean refreshing = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_path = Arrays.asList(
                "https://jingyan.baidu.com/article/f0062228fceb86fbd3f0c8ab.html\n",
                "https://baike.pcbaby.com.cn/qzbd/940112.html\n",
                "https://jingyan.baidu.com/article/afd8f4de2f728634e286e930.html\n",
                "http://bbs.tianya.cn/post-98-1060268-1.shtml\n"
        );
        list_title.add("宝贝成长");
        list_title.add("宝贝饮食");
        list_title.add("宝贝学习");
        list_title.add("宝贝玩具");
        bannerImage = Arrays.asList(
                R.drawable.baby1,
                R.drawable.baby2,
                R.drawable.baby3,
                R.drawable.baby4
        );
        user = BmobUser.getCurrentUser(User.class);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.announce,container,false);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_anno);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshing) {
                    return;
                } else {
                    refreshing = true;
                    initNews();
//                    initDiscuss();
                }
            }
        });
        user = BmobUser.getCurrentUser(User.class);
        banner = (Banner) view.findViewById(R.id.banner);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_first);
//        disRecycler = (RecyclerView) view.findViewById(R.id.recycle_second);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);
//        disRecycler.setLayoutManager(layoutManager);
//        DisAdapter disAdapter = new DisAdapter(getContext(),mComment);
        NewsAdapter adapter = new NewsAdapter(getContext(),posts);
//        disRecycler.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView.addItemDecoration(new SpaceItemDecoration(30));
        recyclerView.setAdapter(adapter);
//        disRecycler.setAdapter(disAdapter);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new MyLoader())
                .setImages(bannerImage)
                .setBannerTitles(list_title)
                .setDelayTime(3000)
                .isAutoPlay(true)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), BannerActivity.class);
                intent.putExtra("bannerUri",list_path.get(position));
                startActivity(intent);
            }
        });
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.post:
                        startActivity(new Intent(getActivity(), PostActivity.class));
                        break;
                }
                return true;
            }
        });
        initNews();
//        initDiscuss();
        return view;
    }
//        public void initDiscuss(){
//
//
//        BmobQuery<Comment> query = new BmobQuery<>();
//        Post post1 = new Post();
//
//
//
//        query.include("post,user");
//        query.order("createdAt");
//        query.findObjects(new FindListener<Comment>() {
//            @Override
//            public void done(List<Comment> list, BmobException e) {
//
//                if(e == null){
//                    for(Comment comment : list){
//                        Log.e("comment2",comment.getContent());
//                        Log.e("comment2",comment.getUser().getUsername());
//                        mComment.add(0,comment);
//                    }
//                    recyclerView.getAdapter().notifyDataSetChanged();
//                    swipe.setRefreshing(false);
//                    refreshing = false;
//                }
//                else{
//                    Log.e("faild",e.toString());
//                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
//                    refreshing = false;
//                    swipe.setRefreshing(false);
//                }
//            }
//        });
//    }
    public void initNews(){
        BmobQuery<Post> query = new BmobQuery<>();
        query.setSkip(posts.size());
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e == null) {
                    for(Post post : list){
                            posts.add(0,post);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    swipe.setRefreshing(false);
                    refreshing = false;
                }
                else{
                        Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                    refreshing = false;
                    swipe.setRefreshing(false);
                    }
                }

        });
    }

}
class MyLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        int imageId = (int) path;
        imageView.setImageResource(imageId);
    }
}
class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    int mSpace;
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }

    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
