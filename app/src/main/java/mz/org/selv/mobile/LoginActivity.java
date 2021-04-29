package mz.org.selv.mobile;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_ACCESS_TOKEN;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_PASSWORD;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_USERNAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import mz.org.selv.mobile.auth.LoginHelper;

public class LoginActivity extends AppCompatActivity {

    private LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clearCredentials();

        loginHelper = new LoginHelper(getApplicationContext());

        Button btLogIn = findViewById(R.id.bt_log_in);
        btLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.tx_username);
                EditText password = findViewById(R.id.tx_password);
                loginHelper.obtainAccessToken(LoginActivity.this, username.getText().toString(), password.getText().toString());
            }
        });

        TextView linkTextView = findViewById(R.id.tv_forgot_password);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void clearCredentials() {
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.remove(KEY_ACCESS_TOKEN);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }
}
