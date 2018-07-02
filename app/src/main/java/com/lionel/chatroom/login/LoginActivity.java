package com.lionel.chatroom.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.ChatActivity;
import com.lionel.chatroom.login.presenter.ILoginPresenter;
import com.lionel.chatroom.login.presenter.LoginPresenterImpl;
import com.lionel.chatroom.login.view.ILoginView;
import com.lionel.chatroom.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private ILoginPresenter loginPresenter;
    private TextInputEditText mEdtEmail, mEdtPassword;
    private TextView mTxtViewId, mTxtViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenterImpl(this);

        initView();
    }

    private void initView() {
        mEdtEmail = findViewById(R.id.edt_signup_email);
        mEdtPassword = findViewById(R.id.edt_signup_password);
        mTxtViewId = findViewById(R.id.txtViewID);
        mTxtViewEmail = findViewById(R.id.txtViewEmail);
    }


    public void onLogin(View view) {
        loginPresenter.login(mEdtEmail.getText().toString(),
                mEdtPassword.getText().toString());
    }

    @Override
    public void onLoginSuccess(String msg, String email) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
        mTxtViewId.setText(msg);
        mTxtViewEmail.setText(email);

        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void doSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
