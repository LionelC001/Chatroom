package com.lionel.chatroom.signup;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lionel.chatroom.R;
import com.lionel.chatroom.signup.presenter.ISignUpPresenter;
import com.lionel.chatroom.signup.presenter.SignUpPresenterImpl;
import com.lionel.chatroom.signup.view.ISignUpView;

public class SignUpFragment extends Fragment implements ISignUpView, View.OnClickListener {

    private ISignUpPresenter signUpPresenter;
    private TextInputEditText mEdtName, mEdtEmail, mEdtPassword;
    private AlertDialog progress;

    public SignUpFragment() {
        signUpPresenter = new SignUpPresenterImpl(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEdtName = getView().findViewById(R.id.edt_signup_name);
        mEdtEmail = getView().findViewById(R.id.edt_signup_email);
        mEdtPassword = getView().findViewById(R.id.edt_signup_password);
        Button mBtnSignUp = getView().findViewById(R.id.btn_sign_up);
        mBtnSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_up) {
            signUpPresenter.signUp(mEdtName.getText().toString(),
                    mEdtEmail.getText().toString(),
                    mEdtPassword.getText().toString());
        }
    }

    @Override
    public void onSignUpSuccess(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        getActivity().finish();
    }

    @Override
    public void onSignUpFailure(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowProgress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        progress = builder.create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_loading_progress, null);
        ((TextView) view.findViewById(R.id.txt_progress_title)).setText("註冊中...");
        ((TextView) view.findViewById(R.id.txt_progress_message)).setText("請稍後...");
        ((ProgressBar) view.findViewById(R.id.progress_bar)).getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorDialogProgress), PorterDuff.Mode.MULTIPLY);
        progress.setView(view);
        progress.setCancelable(false);
        progress.show();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        progress.getWindow().setWindowAnimations(R.style.AnimDialogLoading);
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
