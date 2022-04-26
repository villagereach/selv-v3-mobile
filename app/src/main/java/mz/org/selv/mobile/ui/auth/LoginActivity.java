package mz.org.selv.mobile.ui.auth;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_ACCESS_TOKEN;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_PASSWORD;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_USERNAME;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.MainActivity;
import mz.org.selv.mobile.R;
import mz.org.selv.mobile.auth.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clearCredentials();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //LoginHelper loginHelper = new LoginHelper(getApplicationContext());
        EditText etServerUrl, etUsername, etPassword;
        Button btLogIn = findViewById(R.id.bt_log_in);
        etServerUrl = findViewById(R.id.et_login_server_url);
        etUsername = findViewById(R.id.et_login_username);
        etPassword = findViewById(R.id.et_login_password);
        String lastUsedServer = loginViewModel.getLastUsedServer();
        etServerUrl.setText(lastUsedServer);

        progressDialog = new ProgressDialog(this);


        /*
        btLogIn.setOnClickListener(v -> {
            EditText username = findViewById(R.id.et_login_username);
            EditText password = findViewById(R.id.et_login_password);
            SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
            Editor editor = sharedPrefs.edit();
            editor.putString(KEY_USERNAME, username.getText().toString());
            editor.putString(KEY_PASSWORD, password.getText().toString());
            editor.apply();
            loginHelper.obtainAccessToken(LoginActivity.this);
        });



      //  TextView linkTextView = findViewById(R.id.tv_forgot_password);
      //  linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

         */
        btLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(etServerUrl.getText().toString(), etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        //verify if session changed to login
        final Observer<Boolean> isLoggedInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoogedIn) {
                if (isLoogedIn) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Falha no Login", Toast.LENGTH_LONG).show();
                }
            }
        };
        loginViewModel.getSession().observe(this, isLoggedInObserver);

    }

    private void clearCredentials() {
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    private void login(String serverUrl, String username, String password
    ) {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        progressDialog.setMessage(getString(R.string.string_connecting));
        progressDialog.show();
        loginViewModel.login(serverUrl, username, password);
    }
}
