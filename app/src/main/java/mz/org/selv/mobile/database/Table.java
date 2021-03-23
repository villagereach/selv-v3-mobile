package mz.org.selv.mobile.database;

import android.content.ContentValues;

public interface Table {
    public String getTableName();
    public ContentValues getContentValues();
    public String[] getColumnNames();
}
