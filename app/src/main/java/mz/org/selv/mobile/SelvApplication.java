package mz.org.selv.mobile;

import android.app.Application;

public class SelvApplication extends Application {

  private String accessToken;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
