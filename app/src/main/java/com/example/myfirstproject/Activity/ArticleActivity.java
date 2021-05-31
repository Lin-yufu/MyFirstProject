package com.example.myfirstproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstproject.Activity.Bean.DownloadPassageBean;
import com.example.myfirstproject.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleActivity extends BaseActivity {
    private String articleId;
    private ImageView authorIcon;
    private TextView authorNick;
    private TextView publishTime;
    private TextView title;
    private ImageView articleImage;
    private TextView passage;
    private SwipeRefreshLayout articleSwipe;
    private ScrollView articleLayout;
    private TextView topbarName;
    private ImageView backButton;
    final static int DOWNLOAD_SUCCESS=20;
    final static int DOWNLOAD_FALSE=21;
    //设置handler监听子进程加载数据
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==DOWNLOAD_SUCCESS){
                DownloadPassageBean datas=(DownloadPassageBean) msg.obj;
                title.setText(datas.getTitle());
                authorNick.setText(datas.getAuthorNick());
                publishTime.setText(datas.getPublishTime());
                Picasso.get().load("http://60.205.218.123/myfirstproject/icon/"+datas.getAuthorIcon()+".jpg")
                        .resize(authorIcon.getLayoutParams().width, authorIcon.getLayoutParams().height)
                        .centerInside()
                        .into(authorIcon);
                passage.setText(datas.getPassage());
                if(datas.getArticleImage().equals("no_picture")) {
                    articleImage.setVisibility(View.GONE);
                }
                else{
                    articleImage.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://60.205.218.123/myfirstproject/image/"+datas.getArticleImage()+".jpg")
                            .resize(articleImage.getLayoutParams().width, articleImage.getLayoutParams().height)
                            .centerInside()
                            .into(articleImage);
                }

                articleSwipe.setRefreshing(false);
                articleLayout.setVisibility(View.VISIBLE);

            }else{
                Log.e("download error","download error");
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        articleLayout=(ScrollView)findViewById(R.id.article_layout);
        topbarName=findViewById(R.id.top_bar_name);
        backButton=findViewById(R.id.back_button);
        topbarName.setText("帖子详情");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        Intent intent =getIntent();
        articleId=intent.getStringExtra("articleId");
        authorIcon=(ImageView)findViewById(R.id.detail_author_icon);
        authorNick=(TextView)findViewById(R.id.detail_author_nick);
        publishTime=(TextView)findViewById(R.id.detail_publish_time);
        title=(TextView)findViewById(R.id.detail_article_title);
        articleImage=(ImageView)findViewById(R.id.detail_article_image);
        passage=(TextView)findViewById(R.id.detail_article);
        articleSwipe=(SwipeRefreshLayout)findViewById(R.id.article_swipe);
        //设置进入时刷新
        articleSwipe.setEnabled(false);
        articleLayout.setVisibility(View.GONE);
        articleSwipe.post(new Runnable() {
            @Override
            public void run() {
                articleSwipe.setRefreshing(true);
            }
        });
        //
        initData();



    }
    public void initData(){
        String url = " http://60.205.218.123/myfirstproject/downloadArticle/downloadArticleDetail.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr="{"+"\""+"articleID"+"\""+":"+"\""+articleId+"\""+"}";
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
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(ArticleActivity.this,"网络未连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()) {
                            try {
                                Message message=new Message();
                                String data=response.body().string();
                                System.out.println(data);
                                Gson gson=new Gson();
                                Type type=new TypeToken<DownloadPassageBean>(){
                                }.getType();
                                DownloadPassageBean datas = gson.fromJson(data, type);
                                //获取了数据后发消息给主线程
                                message.what=DOWNLOAD_SUCCESS;
                                message.obj=datas;
                                System.out.println(message.what);
                                System.out.println(message.obj.toString());
                                mhandler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }
}
