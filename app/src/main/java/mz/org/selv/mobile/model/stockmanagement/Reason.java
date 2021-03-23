package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Reason implements Table {
    String category;
    String type;
    String name;
    String id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return Database.Reason.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.Reason.COLUMN_NAME_CATEGORY, category);
        cv.put(Database.Reason.COLUMN_NAME_NAME, name);
        cv.put(Database.Reason.COLUMN_NAME_UUID, id);
        cv.put(Database.Reason.COLUMN_NAME_TYPE, type);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Reason.ALL_COLUMNS;
    }
}
