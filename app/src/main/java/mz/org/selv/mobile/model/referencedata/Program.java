package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Program implements Table {
    private String name;
    private String code;
    private String lastSyncDate;
    private String description;
    private String uuid;
    private String status; // synced not synced
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getTableName() {
        return Database.Program.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.Program.COLUMN_CODE, code);
        cv.put(Database.Program.COLUMN_NAME, name);
        cv.put(Database.Program.COLUMN_ACTIVE, active);
        cv.put(Database.Program.COLUMN_STATUS, status);
        cv.put(Database.Program.COLUMN_UUID, uuid);
        cv.put(Database.Program.COLUMN_DESCRIPTION, description);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Program.ALL_COLUMNS;
    }
}
