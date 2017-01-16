package com.huagu.RX.rongxinmedical.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements RequestListener {


    private final String TAG = "Activity.LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        TextView registeruser = (TextView) this.findViewById(R.id.registeruser);
        TextView findpassword = (TextView) this.findViewById(R.id.findpassword);
        findpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,FindPasswordActivity.class));
            }
        });
        this.findViewById(R.id.registeruser).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {//在此处执行登陆
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            HashMap<String,String> hash = new HashMap<String,String>();
            hash.put("un",mEmailView.getText().toString().trim());
            hash.put("pwd",mPasswordView.getText().toString());
            HttpRequest.getInstance().Request("login",hash,LoginActivity.this);
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
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

    @Override
    public void Success(String method,JSONObject result) throws JSONException {
        Log.e(method,result.toString());
        if (result.getInt("errorCode")==200){
            JSONObject json =  result.getJSONObject("data");
            String USER_ID = json.getString("USER_ID");
            String TYPE = json.getString("TYPE");
            String FIRST_NAME = json.getString("FIRST_NAME");
            String LAST_NAME = json.getString("LAST_NAME");
            String PHOTO = json.has("HEAD_PHOTO")?json.getString("HEAD_PHOTO"):"";
            String ADD_TIME = json.getString("ADD_TIME");
            String IMAGE_PATH = json.getString("imgServer");
            SharedPreferences.Editor se = usershared.edit();
            se.putString("TYPE", TYPE);
            se.putString("FIRST_NAME",FIRST_NAME);
            se.putString("LAST_NAME",LAST_NAME);
            se.putString("PHOTO",PHOTO);
            se.putString("ADD_TIME", ADD_TIME);
            se.putString("IMAGE_PATH", IMAGE_PATH);
            se.putString("USER_ID", USER_ID).commit();
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_tips), Toast.LENGTH_SHORT).show();
            showProgress(false);
            if ("3".equals(TYPE)){
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("pid",USER_ID);
                intent.putExtra("reg_time",ADD_TIME);
                startActivity(intent);
            }else if ("2".equals(TYPE)){
                startActivity(new Intent(LoginActivity.this, PatientListActivity.class));
            }
            setAlias(USER_ID);
            finish();
        }
    }

    @Override
    public void Failure(String str,String method,int errorCode) {
        Log.e(str+method,errorCode+"");
    }

    @Override
    public void Error(String str, String method, Throwable ex) {
        showProgress(false);
        Toast.makeText(LoginActivity.this,ex.getMessage()+"",Toast.LENGTH_SHORT).show();
        Log.e(str+method,ex.getMessage()+"");
    }

    /**]
     * 设置推送使用的别名
     * @param user_id   别名使用的user_id
     */
    private void setAlias(String user_id){
        JPushInterface.setAlias(LoginActivity.this,user_id,tac);
    }


    public TagAliasCallback tac = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String s, Set<String> set) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    settingshared.edit().putString("user_id",s).commit();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAlias(usershared.getString("USER_ID",""));
                        }
                    },30000);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            Log.e(TAG, logs);
        }
    };


}

