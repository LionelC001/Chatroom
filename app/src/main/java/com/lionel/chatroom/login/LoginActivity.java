package com.lionel.chatroom.login;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
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
    private AlertDialog progress;

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
    }


    public void onLogin(View view) {
        loginPresenter.login(mEdtEmail.getText().toString(),
                mEdtPassword.getText().toString());
    }

    @Override
    public void onLoginSuccess(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();

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

    @Override
    public void onShowProgress() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(LoginActivity.this);
        progress = adBuilder.create();
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_loading_progress, null);
        ((TextView) view.findViewById(R.id.txt_progress_title)).setText("登入中");
        ((TextView) view.findViewById(R.id.txt_progress_message)).setText("請稍後...");
        ((ProgressBar) view.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorDialogProgress), PorterDuff.Mode.MULTIPLY);
        progress.setView(view);
        progress.setCancelable(false);
        progress.show();

        //對話框出現時,底下試圖不可操控
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //實現對話框動畫
        Window progressWindow = progress.getWindow();
        progressWindow.setWindowAnimations(R.style.dialogLoadingAnim);
    }

    @Override
    public void onHideProgress() {
        if (progress != null) {
            progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showNeedNetwork() {
        Toast.makeText(this, "網路訊號不穩,請確認網路狀態", Toast.LENGTH_LONG).show();
    }
}
