package mz.org.selv.mobile.service.auth;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mz.org.selv.mobile.model.configuration.Server;
import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.auth.User;

public class AuthService {

    private Context mContext;

    public AuthService(Context context) {
        this.mContext = context;
    }

    public User localLogin(String serverUrl, String username, String password) {
        Database database = new Database(mContext);
        database.open();
        User user = null;
        Server server = getServerByUrl(serverUrl);
        if (server != null) {
            Cursor cursorUser = database.select(User.class, Database.User.COLUMN_USERNAME + "=? AND " + Database.User.COLUMN_SERVER_ID + " =? ", new String[]{username, server.getServerId()},
                    null, null, null);
            if(cursorUser.getCount() == 1){
                cursorUser.moveToFirst();
                user = Converter.cursorToUser(cursorUser);
            }

            cursorUser.close();
            database.close();
            return user;
        } else {
            return null;
        }
    }

    public void logout(String username) {

    }

    public long addServer(String serverUrl) {
        Database database = new Database(mContext);
        database.open();
        Server server = new Server();
        server.setUrl(serverUrl);
        server.setServerId(UUID.randomUUID().toString());
        long insertedId = database.insert(server);
        database.close();
        return insertedId;
    }

    public Server getServerByUrl(String url) {
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Server.class, Database.Server.COLUMN_URL + "=?", new String[]{url}, null, null, null);

        if (cursor.moveToFirst()) {
            Server server = Converter.cursorToServer(cursor);
            cursor.close();
            database.close();
            return server;
        } else {
            cursor.close();
            database.close();
            return null;
        }
    }

    public Server getOrCreateServerByUrl(String url) {
        Database database = new Database(mContext);
        database.open();
        if(database == null){
            System.out.println("database is null");
        }

        Cursor cursor = database.select(Server.class, Database.Server.COLUMN_URL + "=?", new String[]{url}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Server server = Converter.cursorToServer(cursor);
            cursor.close();
            database.close();
            return server;
        } else {
            Server server = new Server();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            server.setLastTimeUsed(dateFormat.format(new Date()));
            server.setUrl(url);
            server.setServerId(UUID.randomUUID().toString());
            if (database.insert(server) > 0) {
                cursor.close();
                database.close();
                return server;
            } else {
                cursor.close();
                database.close();
                return null;
            }
        }
    }


    public Server getLastUsedServer() {
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Server.class, null, null, null, null, null);
        Date maxDate = null;
        Server lastServer = new Server();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            Server server = Converter.cursorToServer(cursor);
            try {
                if (maxDate == null) {
                    maxDate = dateFormat.parse(server.getLastTimeUsed());
                    lastServer = server;
                } else {
                    Date currentDate = dateFormat.parse(server.getLastTimeUsed());
                    if (maxDate.before(currentDate)) {
                        maxDate = currentDate;
                        lastServer = server;
                    }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        cursor.close();
        database.close();
        return lastServer;
    }

    public Map saveSession(String serverUrl, String username, String password, JSONObject userDetails) {
        Server server = getOrCreateServerByUrl(serverUrl);
        User user = getUserByUsernameAndServer(username, server);
        Map<String, String> session = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (user != null) {
            System.out.println("User exists...");
            //check if its local login
            if(userDetails != null){
                try {
                    user.setToken(userDetails.getString("access_token"));
                    user.setTokenExpiration(userDetails.getInt("expires_in"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            user.setPassword(password);
            user.setLastTimeLoggedInServer(dateFormat.format(new Date()));
            updateUser(user);
            session.put("userId", user.getUserId());
            session.put("token", user.getToken());
            session.put("dateServerLogin", user.getLastTimeLoggedInServer());
        } else { // new user
            String lastLoginDate = dateFormat.format(new Date());
            String token;
            int tokenExpiration;
            String userId;
            try {
                tokenExpiration = userDetails.getInt("expires_in");
                userId = userDetails.getString("referenceDataUserId");
                token = userDetails.getString("access_token");
                addUser(server, username, password, userId, token, tokenExpiration, lastLoginDate, null, null, null);
                session.put("userId", userId);
                session.put("token", token);
                session.put("dateServerLogin", lastLoginDate);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return session;
    }

    public User getUserByUsernameAndServer(String username, Server server) {
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(User.class, Database.User.COLUMN_USERNAME + "=? AND " + Database.User.COLUMN_SERVER_ID
                + "=? ", new String[]{username, server.getServerId()}, null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            User user = Converter.cursorToUser(cursor);
            cursor.close();
            database.close();
            return user;
        } else {
            return null;
        }
    }

    public int updateUser(User user) {
        Database database = new Database(mContext);
        database.open();
        int update = database.update(User.class, user.getContentValues(), Database.User.COLUMN_USER_ID + "=?",
                new String[]{user.getUserId()});
        database.close();
        return update;
    }

    public long addUser(Server server, String username, String password, String userId, String token, int tokenExpiration, String lastLoggedIn, String firstName,
                        String lastName, String homeFacilityId) {
        Database database = new Database(mContext);
        database.open();
        User user = new User();
        user.setUsername(username);
        user.setUserId(userId);
        user.setPassword(password);
        user.setLastTimeLoggedInServer(lastLoggedIn);
        System.out.println(user.toString());
        user.setServerId(server.getServerId());
        user.setToken(token);
        user.setTokenExpiration(tokenExpiration);
        long insertedId = database.insert(user);
        database.close();
        return insertedId;
    }
}
