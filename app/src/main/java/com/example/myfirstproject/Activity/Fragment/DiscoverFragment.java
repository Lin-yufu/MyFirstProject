package com.example.myfirstproject.Activity.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myfirstproject.Activity.Adapter.DiscoverContentAdapter;
import com.example.myfirstproject.Activity.Adapter.OnImageClickListener;
import com.example.myfirstproject.Activity.AddArticleActivity;
import com.example.myfirstproject.Activity.Bean.DiscoveryContentBean;
import com.example.myfirstproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.tools.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {
    private List<DiscoveryContentBean> homepageContentList = new ArrayList<>();
    private TextView homepageEmpty;
    private ImageView addButton;
    private TextView topbarName;
    final static int NO_RESULT = 20;
    final static int HAS_RESULT = 21;
    //子线程加载完数据后，发送给主线程更新ui
    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == HAS_RESULT) {
//                Toast.makeText(getActivity(),"消息传入成功",Toast.LENGTH_SHORT).show();
                List<DiscoveryContentBean> datas = (List<DiscoveryContentBean>) msg.obj;
                adapter.setData(datas);
                homepageEmpty = (TextView) getActivity().findViewById(R.id.homepage_empty);
                homepageEmpty.setVisibility(View.GONE);
            } else {
                homepageEmpty = (TextView) getActivity().findViewById(R.id.homepage_empty);
                homepageEmpty.setVisibility(View.VISIBLE);
            }


        }
    };

    public DiscoverFragment() {
        // Required empty public constructor
    }

    private DiscoverContentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        //加载顶部栏
        addButton = (ImageView) view.findViewById(R.id.add_button);
        topbarName = (TextView) view.findViewById(R.id.top_bar_name);
        topbarName.setText("发现");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.discover_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiscoverContentAdapter(homepageContentList);
        recyclerView.setAdapter(adapter);
        //坑点
        adapter.setmOnImageClickListener(new OnImageClickListener() {
            @Override
            public void onImageClicked(View view, int positon, String url) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View imgEntryView = inflater.inflate(R.layout.picture_dialog, null);
                // 加载自定义的布局文件,AlertDialog一定要设置style才能全屏
                AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.MyDialog).create();
                ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);

                //回调glide的SimpleTarget方法，保证加载完成后出现对话框
                Glide.with(getContext()).load(url).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        img.setImageDrawable(resource);
                        dialog.setView(imgEntryView, 0, 0, 0, 0);
                        dialog.show();
                    }
                });
                // 这个是加载网络图片的，可以是自己的图片设置方法
                // imageDownloader.download(imageBmList.get(0),img);
                // 自定义dialog

                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
                //长按,弹出对话框，判断是否保存
                imgEntryView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setTitle("是否保存图片？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bitmap bitmap=((BitmapDrawable)img.getDrawable()).getBitmap();
                                        SimpleDateFormat simpleDateFormatId = new SimpleDateFormat("yyyyMMddHHmmss");
                                        Date date = new Date(System.currentTimeMillis());
                                        String pictureId = simpleDateFormatId.format(date)+ "_jpg";
                                        MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, pictureId, "description");
                                        dialog.dismiss();
                                        Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        Dialog dialog=builder.create();
                        dialog.show();

                        return true;
                    }
                });
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddArticleActivity.class);
                startActivity(intent);

            }
        });
        initData();
        return view;
    }

    //用于fragment切换时重新加载数据
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {  //不在最前端界面显示
            System.out.println("HomepageHidden");
        } else {  //重新显示到最前端
            initData();
        }
    }

    public void initData() {
        String url = " http://60.205.218.123/myfirstproject/downloadArticle/downloadArticleBasic.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    new Thread() {
                        @Override
                        public void run() {

                            try {
                                String back = response.body().string();
                                System.out.println(back);
                                if (back.equals("no_result")) {
                                    System.out.println("No result");
                                    Message message = new Message();
                                    message.what = NO_RESULT;
                                    mhandler.sendMessage(message);

                                } else {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<DiscoveryContentBean>>() {
                                    }.getType();
                                    List<DiscoveryContentBean> datas = gson.fromJson(back, type);
                                    Message message = new Message();
                                    message.what = HAS_RESULT;
                                    message.obj = datas;
                                    mhandler.sendMessage(message);

                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }.start();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }


}





