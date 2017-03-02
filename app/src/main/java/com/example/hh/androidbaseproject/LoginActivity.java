package com.example.hh.androidbaseproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hh.androidbaseproject.Beans.LoginData;
import com.example.hh.androidbaseproject.DataAndHelper.DataCache;
import com.example.hh.androidbaseproject.DataAndHelper.DataCallBack;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        mUsernameView = (EditText)findViewById(R.id.user_name);
        mPasswordView = (EditText)findViewById(R.id.password);

//        mUsernameView.setText("软件开发测试");
//        mUsernameView.setText("刘盼");
//        mUsernameView.setText("黄新军");
//        mPasswordView.setText("1025");

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                DataCache.getInstance().login(mUsernameView.getText().toString(),
                        mPasswordView.getText().toString(), new DataCallBack<LoginData>() {
                            @Override
                            public void onEnd(LoginData data) {
                                showProgress(false);
                                LoginData d = data;
                                Log.e("---login--->","" + d.errcode);
                                if (d.errcode == 0 && d.record.size() > 0){
                                    if (DataCache.getInstance().mainActivity != null) {
                                        DataCache.getInstance().mainActivity.gotoFirstPage = true;
                                        //TODO:finish问题,似乎没问题了
                                        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                    }else{
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putBoolean("gotoFirstPage", true);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, data.errmsg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

