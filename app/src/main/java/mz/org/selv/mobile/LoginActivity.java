package mz.org.selv.mobile;

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
}
