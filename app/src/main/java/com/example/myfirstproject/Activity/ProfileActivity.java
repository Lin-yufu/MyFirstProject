package com.example.myfirstproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myfirstproject.Activity.Bean.MineBean;
import com.example.myfirstproject.Activity.Engine.GlideEngine;
import com.example.myfirstproject.Activity.Fragment.MineFragment;
import com.example.myfirstproject.Activity.Tools.QuickClick;
import com.example.myfirstproject.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends BaseActivity {
    private TextView hostNickText;
    private TextView hostAvatarText;
    private String hostNick;
    private String hostId;
    private String hostAvatar;
    private LinearLayout linearhostNick;
    private LinearLayout linearhostAvatar;
    private CardView saveButton;
    private ImageView backButton;
    private TextView topbarName;
    private ImageView mineIcon;
    private File uploadIcon = null;
    //两个标志变量用于标志是否上传完成
    private Boolean uploadPictureFinish = false;
    private Boolean uploadJsonFinish = false;
    final static int UPLOAD_PICTURE_SUCCESS = 00;
    final static int UPLOAD_PICTURE_FALSE = 01;
    final static int UPLOAD_JSON_SUCCESS = 10;
    final static int UPLOAD_JSON_FALSE = 11;
    //设置handler监听进程是否完成
    private Handler uploadHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPLOAD_PICTURE_SUCCESS) {
                uploadPictureFinish = true;
//                String message = (String) msg.obj;
//                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
            if (msg.what == UPLOAD_JSON_SUCCESS) {
                uploadJsonFinish = true;
//                String message = (String) msg.obj;
//                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
            if (uploadPictureFinish == true && uploadJsonFinish == true) {
                uploadPictureFinish = false;
                uploadJsonFinish = false;
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_profile);
        //接收上一个activity发来的变量
        Intent intent = getIntent();
        hostId = intent.getStringExtra("hostId");
        hostNick = intent.getStringExtra("hostNick");
        hostAvatar = intent.getStringExtra("hostAvatar");
        //绑定
        linearhostNick = (LinearLayout) findViewById(R.id.linear_host_nick);
        linearhostAvatar = (LinearLayout) findViewById(R.id.linear_host_avatar);
        hostNickText = (TextView) findViewById(R.id.host_nick_text);
        hostAvatarText = (TextView) findViewById(R.id.host_avatar_text);
        saveButton = (CardView) findViewById(R.id.topbar_function);
        backButton = (ImageView) findViewById(R.id.back_button);
        topbarName = (TextView) findViewById(R.id.top_bar_name);
        mineIcon = (ImageView) findViewById(R.id.mine_icon);
        //初始化保存按钮和标题栏
        topbarName.setText("修改信息");
        saveButton.setVisibility(View.VISIBLE);
        hostNickText.setText(hostNick);
        hostAvatarText.setText(hostAvatar);


        //设置点击事件
        mineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMineIcon();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuickClick.isFastClick()){
                    saveInfo();
                }
                else{
                    Toast.makeText(ProfileActivity.this,"操作频繁",Toast.LENGTH_SHORT).show();
                }

            }
        });
        linearhostNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickOnClick();
            }
        });
        linearhostAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarOnClick();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void nickOnClick() {
        //初始化dialog

        View dialogView = View.inflate(ProfileActivity.this, R.layout.alter_dialog, null);

        //绑定dialog的事件
        final EditText dialogInputText = (EditText) dialogView.findViewById(R.id.dialog_input_text);
        final TextInputLayout dialogInput = (TextInputLayout) dialogView.findViewById(R.id.dialog_input);
        dialogInput.setCounterEnabled(true);
        dialogInput.setCounterMaxLength(10);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("修改昵称");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = dialogInputText.getText().toString();
                if (inputText.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    hostNickText.setText(inputText);
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

    public void avatarOnClick() {
        View dialogView = View.inflate(ProfileActivity.this, R.layout.alter_dialog, null);

        //绑定dialog的事件
        final EditText dialogInputText = (EditText) dialogView.findViewById(R.id.dialog_input_text);
        final TextInputLayout dialogInput = (TextInputLayout) dialogView.findViewById(R.id.dialog_input);
        dialogInput.setCounterEnabled(true);
        dialogInput.setCounterMaxLength(10);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("修改个性签名");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = dialogInputText.getText().toString();
                if (inputText.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    hostAvatarText.setText(inputText);
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

    public void editMineIcon() {
        PictureSelector.create(ProfileActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .enableCrop(true)
                .isWeChatStyle(true)
                .selectionMode(PictureConfig.SINGLE)
                .withAspectRatio(1, 1)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        System.out.println(result);
                        Glide.with(ProfileActivity.this)
                                .load(result.get(0).getCutPath())
                                .into(mineIcon);
                        //找到图片文件后上传服务器
                        uploadIcon = new File(result.get(0).getCutPath());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    public void saveInfo() {
        uploadPicture();
        uploadJson();

    }

    public void uploadPicture() {
        String url = "http://60.205.218.123/myfirstproject/editProfile/uploadIcon.php";
        if (uploadIcon != null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/jpg"), uploadIcon);

            String uploadIconId = hostId + "_icon";
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("icon", uploadIconId, body)
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
        else {
            Message message=new Message();
            message.what=UPLOAD_PICTURE_SUCCESS;
            message.obj="图片上传成功";
            uploadHandler.sendMessage(message);
        }

    }

    public void uploadJson() {
        String url = "http://60.205.218.123/myfirstproject/editProfile/uploadProfile.php";
        MineBean uploadBean = new MineBean();
        uploadBean.setHostId(hostId);
        uploadBean.setHostIcon(hostId + "_icon");
        uploadBean.setHostNick(hostNickText.getText().toString());
        uploadBean.setHostAvatar(hostAvatarText.getText().toString());
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
                    Log.i("upload", back);
                    Message message = new Message();
                    message.what = UPLOAD_JSON_SUCCESS;
                    message.obj = back;
                    uploadHandler.sendMessage(message);

                }
            }


        });


    }


}
