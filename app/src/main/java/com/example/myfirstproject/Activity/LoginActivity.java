package com.example.myfirstproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstproject.Activity.Bean.HostBasicInfo;
import com.example.myfirstproject.Activity.Tools.QuickClick;
import com.example.myfirstproject.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private Button loginButton;
    private TextView pleaseRegistText;
    private EditText idText,passwordText;
    public static String hostId;
    private TextInputLayout textInputId;
    private TextInputLayout textInputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        //绑定
        idText=(EditText)findViewById(R.id.login_id);
        passwordText=(EditText)findViewById(R.id.login_password);
        loginButton=(Button)findViewById(R.id.check_user);
        pleaseRegistText=(TextView)findViewById(R.id.pleaseRegist);
        textInputId=(TextInputLayout)findViewById(R.id.text_input_id);
        textInputPassword=(TextInputLayout)findViewById(R.id.text_input_password);
        //设置InputTextLayout
        textInputId.setErrorEnabled(true);
        //输入校验
        loginCheck();
        //登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuickClick.isFastClick()){
                    hostId=idText.getText().toString();
                    String password=passwordText.getText().toString();
                    HostBasicInfo hostBasicInfo =new HostBasicInfo(hostId,password);
                    String jsonstr = new Gson().toJson(hostBasicInfo);
                    //System.out.println(jsonstr);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonstr);

                    OkHttpClient client=new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://60.205.218.123/myfirstproject/login.php")
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("error", "onFailure: " + e);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"网络未连接", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        //根据返回值进行响应
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String back = response.body().string();

                            if (back.equals("success")) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("hostId",hostId);
                                startActivity(intent);
                                finish();

                            }
                            else if (back.equals("no_regist")) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                final String message=back.toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"账号未注册", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            else if (back.equals("passwd_error")) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                final String message=back.toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginActivity.this,"操作频繁",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //跳转注册
        pleaseRegistText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });

    }
    //对输入信息进行检查
    public void loginCheck(){
        final String pattern="^[a-z0-9A-Z]+$";

        textInputId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!idText.getText().toString().matches(pattern)) {
                    idText.setError("用户名只能由数字字母构成");
                }
                else{
                    idText.setError(null);
                }
            }
        });
    }
}
