package com.lzp.smp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lzp.smp.R;
import com.lzp.smp.util.SPUtil;

public class LoginActivity extends AppCompatActivity {
    public static final String ACCOUNT_STR = "admin";
    public static final String PASSWORD_STR = "123456";

    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private RadioGroup radio_group;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
//        startActivity(intent);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        radio_group = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAccount();
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rb2.getId()) {
                    type = 1;
                } else if (i == rb3.getId()) {
                    type = 2;
                } else {
                    type = 0;
                }
            }
        });
    }

    private void verifyAccount() {
        String accountStr = et_account.getText().toString();
        String passwordStr = et_password.getText().toString();
        String passwordDB = SPUtil.getInstance(this).getValue(accountStr);
        if (TextUtils.isEmpty(accountStr)) {
            Toast.makeText(this, "请输入账号", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(passwordStr)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
        } else if (TextUtils.equals(accountStr, ACCOUNT_STR) && TextUtils.equals(passwordStr, PASSWORD_STR)) {
            goLogin();
        } else if (!TextUtils.isEmpty(passwordDB) && TextUtils.equals(passwordStr, passwordDB)) {
//            addAccountToDB(accountStr, passwordStr);
            goLogin();
        } else {
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_LONG).show();
        }
    }

    private void goLogin() {
//        if (type == 1) {
//            Toast.makeText(this, "跳到教师页面", Toast.LENGTH_LONG).show();
//        } else if (type == 2) {
//            Toast.makeText(this, "跳到学生页面", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "跳到管理员页面", Toast.LENGTH_LONG).show();
//        }
        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
        startActivity(intent);
    }

    private void addAccountToDB(String accountStr, String passwordStr) {
        SPUtil.getInstance(this).setValue(accountStr, passwordStr);
    }
}