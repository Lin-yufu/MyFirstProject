package com.example.myfirstproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstproject.Activity.Adapter.CommentAdapter;
import com.example.myfirstproject.Activity.Bean.DownloadPassageBean;
import com.example.myfirstproject.Activity.Bean.DownloadReviewBean;
import com.example.myfirstproject.Activity.Bean.ReviewBean;
import com.example.myfirstproject.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myfirstproject.Activity.MainActivity.hostId;

public class ArticleActivity extends BaseActivity {
    private List<DownloadReviewBean> mDownlaodReviewContentList = new ArrayList<>();
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
    private CardView commentButton;
    private String commentId;
    private ReviewBean reviewBean;
    private CommentAdapter adapter;
    final static int ARTICLE_DOWNLOAD_SUCCESS = 20;
    final static int ARTICLE_DOWNLOAD_FALSE = 21;
    final static int COMMENT_DOWNLOAD_SUCCESS = 22;
    final static int COMMENT_DOWNLOAD_FALSE = 23;
    private Boolean downloadPassageSuccess=false;
    private Boolean downloadCommentSuccess=false;

    //设置handler监听子进程加载数据
    private Handler mhandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {

            super.handleMessage(msg);
            if (msg.what == ARTICLE_DOWNLOAD_SUCCESS) {
                downloadPassageSuccess=true;
                DownloadPassageBean downloadPassageBean= (DownloadPassageBean) msg.obj;
                title.setText(downloadPassageBean.getTitle());
                authorNick.setText(downloadPassageBean.getAuthorNick());
                publishTime.setText(downloadPassageBean.getPublishTime());
                Picasso.get().load("http://60.205.218.123/myfirstproject/icon/" + downloadPassageBean.getAuthorIcon() + ".jpg")
                        .resize(authorIcon.getLayoutParams().width, authorIcon.getLayoutParams().height)
                        .centerInside()
                        .into(authorIcon);
                passage.setText(downloadPassageBean.getPassage());
                if (downloadPassageBean.getArticleImage().equals("no_picture")) {
                    articleImage.setVisibility(View.GONE);
                } else {
                    articleImage.setVisibility(View.VISIBLE);

                    Picasso.get().load("http://60.205.218.123/myfirstproject/image/" + downloadPassageBean.getArticleImage() + ".jpg")
                            .fit()
                            .centerInside()
                            .into(articleImage);

                }
                articleLayout.setVisibility(View.VISIBLE);
                articleSwipe.setEnabled(false);
            }
            if(msg.what==COMMENT_DOWNLOAD_SUCCESS){
                downloadCommentSuccess=true;
                List<DownloadReviewBean> downloadReviewBeans= (List<DownloadReviewBean>) msg.obj;
                adapter.setData(downloadReviewBeans);

            }
            if (downloadPassageSuccess == true && downloadCommentSuccess == true){
                downloadCommentSuccess=false;
                downloadPassageSuccess=false;
                System.out.println("两个变量被重置为false");


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        articleLayout = (ScrollView) findViewById(R.id.article_layout);
        commentButton = (CardView) findViewById(R.id.comment_button);
        topbarName = findViewById(R.id.top_bar_name);
        backButton = findViewById(R.id.back_button);
        topbarName.setText("帖子详情");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        articleId = intent.getStringExtra("articleId");
        authorIcon = (ImageView) findViewById(R.id.detail_author_icon);
        authorNick = (TextView) findViewById(R.id.detail_author_nick);
        publishTime = (TextView) findViewById(R.id.detail_publish_time);
        title = (TextView) findViewById(R.id.detail_article_title);
        articleImage = (ImageView) findViewById(R.id.detail_article_image);
        passage = (TextView) findViewById(R.id.detail_article);
        articleSwipe = (SwipeRefreshLayout) findViewById(R.id.article_swipe);
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
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = View.inflate(ArticleActivity.this, R.layout.comment_dialog, null);

                //绑定dialog的事件
                final EditText dialogInputText = (EditText) dialogView.findViewById(R.id.dialog_input_text);
                final TextInputLayout dialogInput = (TextInputLayout) dialogView.findViewById(R.id.dialog_input);
                dialogInput.setCounterEnabled(true);
                dialogInput.setCounterMaxLength(100);
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);
                builder.setTitle("评论");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputText = dialogInputText.getText().toString();
                        if (inputText.isEmpty()) {
                            Toast.makeText(ArticleActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            SimpleDateFormat simpleDateFormatId = new SimpleDateFormat("yyyyMMddHHmmss");
                            //根据时间生成评论id
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = simpleDateFormatId.format(date);
                            commentId = currentTime + articleId;
                            reviewBean = new ReviewBean();
                            reviewBean.setCommentId(commentId);
                            reviewBean.setReviewContent(inputText);
                            reviewBean.setArticleId(articleId);
                            reviewBean.setReviewerId(hostId);
                            reviewBean.setReviewTime(currentTime);
                            sendComment(reviewBean);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setView(dialogView);
                dialog.show();

            }
        });
        //评论的recyclerview
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.comment_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ArticleActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(mDownlaodReviewContentList);
        recyclerView.setAdapter(adapter);
        initData();
        initComment(articleId);

        //长按,弹出对话框，判断是否保存图片
        articleImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(ArticleActivity.this);
                builder.setTitle("是否保存图片？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bitmap bitmap=((BitmapDrawable)articleImage.getDrawable()).getBitmap();
                                SimpleDateFormat simpleDateFormatId = new SimpleDateFormat("yyyyMMddHHmmss");
                                Date date = new Date(System.currentTimeMillis());
                                String pictureId = simpleDateFormatId.format(date)+ "_jpg";
                                MediaStore.Images.Media.insertImage(ArticleActivity.this.getContentResolver(), bitmap, pictureId, "description");
                                dialog.dismiss();
                                Toast.makeText(ArticleActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
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

    public void initData() {
        String url = " http://60.205.218.123/myfirstproject/downloadArticle/downloadArticleDetail.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr = "{" + "\"" + "articleID" + "\"" + ":" + "\"" + articleId + "\"" + "}";
        System.out.println(uploadstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), uploadstr);
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
                        Toast.makeText(ArticleActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                new Thread() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                Message message = new Message();
                                String data = response.body().string();
                                System.out.println(data);
                                Gson gson = new Gson();
                                Type type = new TypeToken<DownloadPassageBean>() {
                                }.getType();
                                DownloadPassageBean datas = gson.fromJson(data, type);
                                //获取了数据后发消息给主线程
                                message.what = ARTICLE_DOWNLOAD_SUCCESS;
                                message.obj = datas;
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

    private void sendComment(ReviewBean reviewBean) {
        String url = "http://60.205.218.123/myfirstproject/uploadComment.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String jsonstr = new Gson().toJson(reviewBean);
        System.out.println(jsonstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonstr);

        Request request = new Request.Builder()
                .url(url)
                .post(body)//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Connect_error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    String back = response.body().string();
                    System.out.println(back);
                    initComment(articleId);

                }
            }


        });

    }

    public void initComment(String articleId) {
        String url = "http://60.205.218.123/myfirstproject/downloadComment.php";
        OkHttpClient okHttpClient = new OkHttpClient();
        String uploadstr = "{" + "\"" + "articleId" + "\"" + ":" + "\"" + articleId + "\"" + "}";
        System.out.println(uploadstr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), uploadstr);
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
                                    message.what = COMMENT_DOWNLOAD_FALSE;
                                    mhandler.sendMessage(message);

                                } else {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<DownloadReviewBean>>() {
                                    }.getType();
                                    List<DownloadReviewBean> datas = gson.fromJson(back, type);
                                    Message message = new Message();
                                    message.what = COMMENT_DOWNLOAD_SUCCESS;
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
}
