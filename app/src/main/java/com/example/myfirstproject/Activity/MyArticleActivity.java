package com.example.myfirstproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfirstproject.Activity.Adapter.MyArticleAdapter;
import com.example.myfirstproject.Activity.Adapter.OnDeleteClickListener;
import com.example.myfirstproject.Activity.Bean.DiscoveryContentBean;
import com.example.myfirstproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myfirstproject.Activity.MainActivity.hostId;

public class MyArticleActivity extends BaseActivity {
    private List<DiscoveryContentBean> myArticleContentList = new ArrayList<>();
    private TextView myArticleEmpty;
    private ImageView backButton;
    private TextView topbarName;
    private CardView commentButton;
    final static int NO_RESULT=20;
    final static int HAS_RESULT=21;
    final static  int RELOAD=23;
    //子线程加载完数据后，发送给主线程更新ui
    public Handler mhandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==HAS_RESULT){
//                Toast.makeText(getActivity(),"消息传入成功",Toast.LENGTH_SHORT).show();
                List<DiscoveryContentBean> datas=(List<DiscoveryContentBean>)msg.obj;
                adapter.setData(datas);
                myArticleEmpty=(TextView)findViewById(R.id.my_article_empty);
                myArticleEmpty.setVisibility(View.GONE);
            }else if(msg.what==NO_RESULT){
                myArticleEmpty=(TextView)findViewById(R.id.my_article_empty);
                myArticleEmpty.setVisibility(View.VISIBLE);
            }else {
                initData();
            }


        }
    };
    private MyArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_article);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_article_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyArticleActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyArticleAdapter(myArticleContentList);
        adapter.setmOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClicked(View view, int positon){
                AlertDialog.Builder builder=new AlertDialog.Builder(MyArticleActivity.this);
                builder.setTitle("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String articleId=myArticleContentList.get(positon).getArticleId();
                                uploadDelete(articleId);
                                adapter.removeItem(positon);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();


            }
        });
        backButton=(ImageView)findViewById(R.id.back_button);
        commentButton=(CardView)findViewById(R.id.comment_button);
        topbarName=(TextView)findViewById(R.id.top_bar_name);
        topbarName.setText("我的文章");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        initData();
    }
    public void initData() {
        String url = "http://60.205.218.123/myfirstproject/myArticle/getMyArticle.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr="{"+"\""+"hostId"+"\""+":"+"\""+hostId+"\""+"}";
        System.out.println(uploadstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),uploadstr);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)//默认就是GET请求，可以不写
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
    public void uploadDelete(String articleId){
        String url = " http://60.205.218.123/myfirstproject/myArticle/deleteMyArticle.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr="{"+"\""+"articleId"+"\""+":"+"\""+articleId+"\""+"}";
        System.out.println(uploadstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),uploadstr);
        Request request = new Request.Builder()
                .url(url)
                .post(body)//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()) {
                            try {
                                String back=response.body().string();
                                System.out.println(back);
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
