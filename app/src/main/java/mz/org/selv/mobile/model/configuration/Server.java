package mz.org.selv.mobile.model.configuration;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Server implements Table {
    String url;
    String serverId;
    String lastTimeUsed;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getLastTimeUsed() {
        return lastTimeUsed;
    }

    public void setLastTimeUsed(String lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }

    @Override
    public String getTableName() {
        return Database.Server.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.Server.COLUMN_LAST_TIME_USED, lastTimeUsed);
        cv.put(Database.Server.COLUMN_SERVER_ID, serverId);
        cv.put(Database.Server.COLUMN_URL, url);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Server.ALL_COLUMNS;
    }
}
