package com.lionel.chatroom.signup;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.signup.presenter.ISignUpPresenter;
import com.lionel.chatroom.signup.presenter.SignUpPresenterImpl;
import com.lionel.chatroom.signup.view.ISignUpView;

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private ISignUpPresenter signUpPresenter;
    private TextInputEditText mEdtName, mEdtEmail, mEdtPassword;

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
}
