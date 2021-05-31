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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstproject.Activity.Bean.HostBasicInfo;
import com.example.myfirstproject.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistActivity extends BaseActivity {
    private TextView registId;
    private TextView registNickname;
    private TextView registAvatar;
    private TextView registPassword1;
    private TextView registPassword2;
    private Button registButton;
    private HostBasicInfo registInfo;
    private String password;
    private String comfirmPassword;
    public static String hostId;
    private TextInputLayout inputRegistId;
    private TextInputLayout inputRegistPassword1;
    private TextInputLayout inputRegistPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        //绑定
        registId = (TextView) findViewById(R.id.regist_id);
        registPassword1 = (TextView) findViewById(R.id.regist_password1);
        registPassword2 = (TextView) findViewById(R.id.regist_password2);
        registButton = (Button) findViewById(R.id.regist_button);
        inputRegistId = (TextInputLayout) findViewById(R.id.input_regist_id);
        inputRegistPassword1 = (TextInputLayout) findViewById(R.id.input_regist_password1);
        inputRegistPassword2 = (TextInputLayout) findViewById(R.id.input_regist_password2);
        //数据验证
        registCheck();


        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostId = registId.getText().toString();
                password = registPassword1.getText().toString();
                comfirmPassword = registPassword2.getText().toString();
                System.out.println(password.equals(comfirmPassword));
                if (password.equals(comfirmPassword) != true) {
                    Toast.makeText(RegistActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    HostBasicInfo hostBasicInfo = new HostBasicInfo(hostId, password);
                    String jsonstr = new Gson().toJson(hostBasicInfo);
                    //System.out.println(jsonstr);
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonstr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://60.205.218.123/myfirstproject/regist.php")
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("error", "onFailure: " + e);

                        }

                        //根据返回值进行响应
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String back = response.body().string();

                            if (back.equals("success")) {
                                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(intent);
                                //finish();

                            } else if (back.equals("has_registed")) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                final String message = back.toString();
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(RegistActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }

    //注册检查
    public void registCheck() {
        final String pattern = "^[a-z0-9A-Z]+$";
        inputRegistId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!registId.getText().toString().matches(pattern)) {
                    registId.setError("用户名只能由数字字母构成");
                } else {
                    registId.setError(null);
                }
            }
        });
        inputRegistPassword2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!registPassword1.getText().toString().equals(registPassword2.getText().toString())) {
                    inputRegistPassword2.setError("密码前后不一致");
                } else {
                    inputRegistPassword2.setError(null);
                }
            }
        });
    }
}
