package com.lionel.chatroom.login;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.chat.ChatActivity;
import com.lionel.chatroom.login.presenter.ILoginPresenter;
import com.lionel.chatroom.login.presenter.LoginPresenterImpl;
import com.lionel.chatroom.login.view.ILoginView;


public class LoginFragment extends Fragment implements ILoginView, View.OnClickListener {
    private ILoginPresenter loginPresenter;
    private TextInputEditText mEdtEmail, mEdtPassword;
    private AlertDialog progress;

    public LoginFragment() {
        loginPresenter = new LoginPresenterImpl(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEdtEmail = getView().findViewById(R.id.edt_signup_email);
        mEdtPassword = getView().findViewById(R.id.edt_signup_password);
        Button mBtnLogin = getView().findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_login) {
            loginPresenter.login(mEdtEmail.getText().toString(),
                    mEdtPassword.getText().toString());
        }
    }

    @Override
    public void onLoginSuccess(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowProgress() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
        progress = adBuilder.create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_progress, null);
        ((TextView) view.findViewById(R.id.txt_progress_title)).setText("登入中");
        ((TextView) view.findViewById(R.id.txt_progress_message)).setText("請稍後...");
        ((ProgressBar) view.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorDialogProgress), PorterDuff.Mode.MULTIPLY);
        progress.setView(view);
        progress.setCancelable(false);
        progress.show();

        //對話框出現時,底下視圖不可操控
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //實現對話框動畫
        Window progressWindow = progress.getWindow();
        progressWindow.setWindowAnimations(R.style.AnimDialogLoading);
    }

    @Override
    public void onHideProgress() {
        if (progress != null) {
            progress.dismiss();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void showNeedNetwork() {
        Toast.makeText(getActivity(), "網路訊號不穩,請確認網路狀態", Toast.LENGTH_LONG).show();
    }
}
