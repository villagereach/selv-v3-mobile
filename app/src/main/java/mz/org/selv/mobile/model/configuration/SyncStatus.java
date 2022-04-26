package mz.org.selv.mobile.model.configuration;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;
/*
this class will hold the status of syncronization for each entity (Reference Data, Facilities, Servers Etc)
 */
public class SyncStatus implements Table {
    String entity; // namer of the entity to sync
    String date; //date of the last successfull sync
    String userId; // user who synced
    String serverId; // server of the user o synced

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String getTableName() {
        return Database.SyncStatus.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.SyncStatus.COLUMN_ENTITY, entity);
        cv.put(Database.SyncStatus.COLUMN_DATE, date );
        cv.put(Database.SyncStatus.COLUMN_USER_ID, userId);
        cv.put(Database.SyncStatus.COLUMN_SERVER_ID,  serverId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.SyncStatus.ALL_COLUMNS;
    }
}
