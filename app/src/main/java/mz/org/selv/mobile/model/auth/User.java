package mz.org.selv.mobile.model.auth;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class User implements Table {
    String username;
    String password;
    String homeFacilityId;
    String homeFacilityName;
    String name;
    String surname;
    String token;
    String isLoggedIn;
    String lastTimeLoggedInServer;
    String userId;
    String serverId;
    int tokenExpiration;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public int getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(int tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastTimeLoggedInServer() {
        return lastTimeLoggedInServer;
    }

    public void setLastTimeLoggedInServer(String lastTimeLoggedInServer) {
        this.lastTimeLoggedInServer = lastTimeLoggedInServer;
    }

    public String getHomeFacilityId() {
        return homeFacilityId;
    }

    public void setHomeFacilityId(String homeFacilityId) {
        this.homeFacilityId = homeFacilityId;
    }

    public String getHomeFacilityName() {
        return homeFacilityName;
    }

    public void setHomeFacilityName(String homeFacilityName) {
        this.homeFacilityName = homeFacilityName;
    }

    public String getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(String isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getTableName() {
        return Database.User.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.User.COLUMN_USERNAME, username);
        cv.put(Database.User.COLUMN_LAST_TIME_LOGGED_IN_SERVER, homeFacilityName);
        cv.put(Database.User.COLUMN_IS_LOGGED_IN, isLoggedIn);
        cv.put(Database.User.COLUMN_SURNAME, surname);
        cv.put(Database.User.COLUMN_TOKEN, token);
        cv.put(Database.User.COLUMN_HOME_FACLITY_ID, homeFacilityId);
        cv.put(Database.User.COLUMN_LAST_TIME_LOGGED_IN_SERVER, lastTimeLoggedInServer);
        cv.put(Database.User.COLUMN_NAME, name);
        cv.put(Database.User.COLUMN_USER_ID, userId);
        cv.put(Database.User.COLUMN_TOKEN_EXPIRATION, tokenExpiration);
        cv.put(Database.User.COLUMN_SERVER_ID, serverId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.User.ALL_COLUMNS;
    }
}
