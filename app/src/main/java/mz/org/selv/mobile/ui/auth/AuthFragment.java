package mz.org.selv.mobile.ui.auth;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import mz.org.selv.mobile.R;

public class AuthFragment extends Fragment {

  private AuthViewModel authViewModel;
  private EditText username;
  private EditText password;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInsanceState) {
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    View root = inflater.inflate(R.layout.fragment_auth, container, false);

    Button btLogIn = root.findViewById(R.id.bt_log_in);
    btLogIn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        username = root.findViewById(R.id.tx_username);
        password = root.findViewById(R.id.tx_password);
        authViewModel.obtainAccessToken(username.getText().toString(), password.getText().toString());
      }
    });

    TextView linkTextView = root.findViewById(R.id.tv_forgot_password);
    linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

    return root;
  }
}
