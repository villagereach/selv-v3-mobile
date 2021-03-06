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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import mz.org.selv.mobile.auth.LoginHelper;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clearCredentials();

        LoginHelper loginHelper = new LoginHelper(getApplicationContext());

        Button btLogIn = findViewById(R.id.bt_log_in);
        btLogIn.setOnClickListener(v -> {
            EditText username = findViewById(R.id.tx_username);
            EditText password = findViewById(R.id.tx_password);
            SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
            Editor editor = sharedPrefs.edit();
            editor.putString(KEY_USERNAME, username.getText().toString());
            editor.putString(KEY_PASSWORD, password.getText().toString());
            editor.apply();
            loginHelper.obtainAccessToken(LoginActivity.this);
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
