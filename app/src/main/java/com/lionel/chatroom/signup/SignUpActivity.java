package com.lionel.chatroom.signup;

import android.graphics.PorterDuff;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.signup.presenter.ISignUpPresenter;
import com.lionel.chatroom.signup.presenter.SignUpPresenterImpl;
import com.lionel.chatroom.signup.view.ISignUpView;

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private ISignUpPresenter signUpPresenter;
    private TextInputEditText mEdtName, mEdtEmail, mEdtPassword;
    private AlertDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpPresenter = new SignUpPresenterImpl(this);

        initView();
    }

    private void initView() {
        mEdtName = findViewById(R.id.edt_signup_name);
        mEdtEmail = findViewById(R.id.edt_signup_email);
        mEdtPassword = findViewById(R.id.edt_signup_password);
    }

    @Override
    public void onSignUpSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSignUpFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void onSignUp(View view) {
        signUpPresenter.signUp(mEdtName.getText().toString(),
                mEdtEmail.getText().toString(),
                mEdtPassword.getText().toString());
    }

    public void onCancel(View view) {
        finish();
    }

    @Override
    public void onShowProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        progress = builder.create();
        LayoutInflater inflater = LayoutInflater.from(SignUpActivity.this);
        View view = inflater.inflate(R.layout.dialog_loading_progress, null);
        ((TextView)view.findViewById(R.id.txt_progress_title)).setText("註冊中...");
        ((TextView)view.findViewById(R.id.txt_progress_message)).setText("請稍後...");
        ((ProgressBar)view.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorDialogProgress), PorterDuff.Mode.MULTIPLY);
        progress.setView(view);
        progress.setCancelable(false);
        progress.show();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progress.getWindow().setWindowAnimations(R.style.dialogLoadingAnim);
    }

    @Override
    public void onHideProgress() {
        if(progress!= null) {
            progress.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showNeedNetwork() {
        Toast.makeText(this,"網路訊號不穩,請確認網路狀態", Toast.LENGTH_LONG).show();
    }
}
