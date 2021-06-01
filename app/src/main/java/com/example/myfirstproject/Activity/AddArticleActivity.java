package com.example.myfirstproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfirstproject.Activity.Bean.UploadPassageBean;
import com.example.myfirstproject.Activity.Engine.GlideEngine;
import com.example.myfirstproject.Activity.Tools.QuickClick;
import com.example.myfirstproject.R;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myfirstproject.Activity.MainActivity.hostId;

public class AddArticleActivity extends BaseActivity {
    private EditText uploadTitle;
    private EditText uploadPassage;
    private CardView upload;
    private ImageView uploadPicture;
    public static final int CHOOSE_PHOTO = 2;
    private String imagePath = null;
    private File picture = null;
    private String pictureId = null;
    private ImageView backButton;
    private LocalMedia selectList = new LocalMedia();
    private TextView topbarName;
    //两个标志变量设置是否上传完成
    private Boolean uploadPictureFinish = false;
    private Boolean uploadJsonFinish = false;
    final static int UPLOAD_PICTURE_SUCCESS = 20;
    final static int UPLOAD_PICTURE_FALSE = 21;
    final static int UPLOAD_JSON_SUCCESS = 30;
    final static int UPLOAD_JSON_FALSE = 31;

    //设置handler监听进程是否完成
    private Handler uploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPLOAD_PICTURE_SUCCESS) {
                uploadPictureFinish = true;
//                String message = (String) msg.obj;
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
            if (msg.what == UPLOAD_JSON_SUCCESS) {
                uploadJsonFinish = true;
//                String message = (String) msg.obj;
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
            if (uploadPictureFinish == true && uploadJsonFinish == true) {
                uploadPictureFinish = false;
                uploadJsonFinish = false;
                //清空内容并发土司说成功了
                uploadTitle.setText("");
                uploadPassage.setText("");
                uploadPicture.setImageResource(R.drawable.select_pic);
                Toast.makeText(AddArticleActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_add_article);
        uploadTitle = findViewById(R.id.upload_title);
        uploadPassage = findViewById(R.id.upload_passage);
        upload = findViewById(R.id.topbar_function);
        uploadPicture =findViewById(R.id.choose_picture);
        topbarName=findViewById(R.id.top_bar_name);
        topbarName.setText("发布");
        upload.setVisibility(View.VISIBLE);
        backButton=findViewById(R.id.back_button);
        //设置返回按钮监听
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取相机权限
                loadLocalPicture();

            }

        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuickClick.isFastClick()){
                    UploadPassageBean uploadPassageBean = new UploadPassageBean();
                    upload.setEnabled(false);
                    //获取时间戳
                    SimpleDateFormat simpleDateFormatId = new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("yyyy年MM月dd日");
                    // HH:mm:ss
                    //获取当前时间
                    Date date = new Date(System.currentTimeMillis());
                    String articleId = simpleDateFormatId.format(date) + hostId;
                    String publishTime = simpleDateFormatTime.format(date);
                    //检查标题内容，修改异常值后再上传
                    String title = uploadTitle.getText().toString();
                    System.out.println(title);
                    if (!title.equals("")) uploadPassageBean.setTitle(title);
                    else uploadPassageBean.setTitle("我就不写标题，就是玩");

                    String passage = uploadPassage.getText().toString();
                    if (!passage.equals("")) uploadPassageBean.setPassage(passage);
                    else uploadPassageBean.setPassage("内容也没有，就是玩");
                    if (pictureId != null) uploadPassageBean.setArticleImage(pictureId);
                    else uploadPassageBean.setArticleImage("no_picture");
                    uploadPassageBean.setHostId(hostId);
                    uploadPassageBean.setArticleId(articleId);
                    uploadPassageBean.setPublishTime(publishTime);
                    upload.setEnabled(true);
                    //上传数据
                    uploadJson(uploadPassageBean);
                    uploadPicture();


                }
                else{
                    Toast.makeText(AddArticleActivity.this,"操作频繁",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void uploadJson(UploadPassageBean uploadBean) {
        String url = "http://60.205.218.123/myfirstproject/uploadPassage/uploadPassage.php";
        uploadBean.setHostId(hostId);
        OkHttpClient okHttpClient = new OkHttpClient();
        String jsonstr = new Gson().toJson(uploadBean);
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
                    Message message = new Message();
                    message.what = UPLOAD_JSON_SUCCESS;
                    message.obj = "杰森上传成功";
                    uploadHandler.sendMessage(message);

                }
            }


        });
    }

    public void uploadPicture() {
        String url = "http://60.205.218.123/myfirstproject/uploadPassage/uploadPicture.php";
        if (picture != null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/jpg"), picture);

            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("picture", pictureId, body)
                    .build();
            System.out.println(multipartBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(multipartBody)//默认就是GET请求，可以不写
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Connect_error");
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String back = response.body().string();
                    Message message = new Message();
                    message.what = UPLOAD_PICTURE_SUCCESS;
                    message.obj = "图片上传成功";
                    uploadHandler.sendMessage(message);
                }
            });
        }
        //图片为空则直接判定图片传输成功了
        else {
            Message message = new Message();
            message.what = UPLOAD_PICTURE_SUCCESS;
            message.obj = "图片上传成功";
            uploadHandler.sendMessage(message);
        }

    }

    public void loadLocalPicture() {
        PictureSelector.create(AddArticleActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        System.out.println(result);
                        Glide.with(AddArticleActivity.this)
                                .load(result.get(0).getPath())
                                .into(uploadPicture);
                        //找到图片文件后上传服务器
                        picture = new File(result.get(0).getRealPath());
                        //给图片设置Id
                        SimpleDateFormat simpleDateFormatId = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = new Date(System.currentTimeMillis());
                        pictureId = simpleDateFormatId.format(date) + hostId + "_jpg";
                    }

                    @Override
                    public void onCancel() {
                    }
                });
    }
}
