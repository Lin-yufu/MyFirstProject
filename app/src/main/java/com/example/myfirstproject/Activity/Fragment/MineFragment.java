package com.example.myfirstproject.Activity.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstproject.Activity.ActivityCollector;
import com.example.myfirstproject.Activity.Bean.MineBean;
import com.example.myfirstproject.Activity.LoginActivity;
import com.example.myfirstproject.Activity.MainActivity;
import com.example.myfirstproject.Activity.MyArticleActivity;
import com.example.myfirstproject.Activity.ProfileActivity;
import com.example.myfirstproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.example.myfirstproject.Activity.MainActivity.hostId;
/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {
    private MineBean mineBeanList= new MineBean();
    private TextView hostNick;
    private TextView hostAvatar;
    private ImageView mineIcon;
    private ImageView musicButton;
    private Button logoutButton;
    private SwipeRefreshLayout mineSwipe;
    private FrameLayout mineLayout;
    private Button editProfile;
    private Button myArticle;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mine, container, false);
        mineSwipe=(SwipeRefreshLayout) view.findViewById(R.id.mine_swipe);
        mineLayout=(FrameLayout)view.findViewById(R.id.mine_layout);
        //设置进入时刷新
        mineLayout.setVisibility(View.GONE);
        mineSwipe.post(new Runnable() {
            @Override
            public void run() {
                mineSwipe.setRefreshing(true);
            }
        });
        //绑定
        hostNick=(TextView) view.findViewById(R.id.mine_nick);
        hostAvatar=(TextView) view.findViewById(R.id.mine_avatar);
        mineIcon=(ImageView)view.findViewById(R.id.mine_icon);
        logoutButton=(Button)view.findViewById(R.id.logout);

        System.out.println(hostId);
        initData();
        //设置按钮监听
        musicButton=(ImageView)view.findViewById(R.id.music);
        final MediaPlayer musicPlayer = MediaPlayer.create(getActivity(), R.raw.bgm);
        //音乐播放器
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicPlayer.isPlaying()){
                    musicPlayer.pause();
                }
                else{
                    musicPlayer.start();
                }

            }
        });
        editProfile=(Button)view.findViewById(R.id.edit_profile);
        //登出按钮
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                ActivityCollector.finishAll();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //编辑资料
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("hostId",hostId);
                intent.putExtra("hostNick",hostNick.getText());
                intent.putExtra("hostAvatar",hostAvatar.getText());
                startActivity(intent);
            }
        });
        myArticle=(Button)view.findViewById(R.id.my_article);
        //我的文章
        myArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyArticleActivity.class);
                intent.putExtra("hostId",hostId);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    public void initData(){

        String url = " http://60.205.218.123/myfirstproject/getHostInformation.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr="{"+"\""+"hostID"+"\""+":"+"\""+hostId+"\""+"}";
        System.out.println(uploadstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),uploadstr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        mineSwipe.setEnabled(false);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "onFailure: " + e);

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()) {
                            try {
                                String data=response.body().string();
                                Gson gson=new Gson();
                                Type type=new TypeToken<MineBean>(){
                                }.getType();
                                MineBean datas = gson.fromJson(data, type);
                                hostNick.setText(datas.getHostNick());
                                hostAvatar.setText(datas.getHostAvatar());
                                Picasso.get().load("http://60.205.218.123/myfirstproject/icon/"+datas.getHostIcon()+".jpg")
                                        .resize(mineIcon.getLayoutParams().width, mineIcon.getLayoutParams().height)
                                        .centerInside()
                                        .into(mineIcon);
                                mineLayout.setVisibility(View.VISIBLE);
                                mineSwipe.setRefreshing(false);





                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });


    }


}
