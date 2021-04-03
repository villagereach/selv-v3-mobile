package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Orderable implements Table {
        private String name;
        private String code;
        private String uuid;

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

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
        }

        @Override
        public String getTableName() {
                return Database.Orderable.TABLE_NAME;
        }

        @Override
        public ContentValues getContentValues() {
                ContentValues cv = new ContentValues();
                cv.put(Database.Orderable.COLUMN_CODE, code);
                cv.put(Database.Orderable.COLUMN_NAME, name);
                cv.put(Database.Orderable.COLUMN_UUID, uuid);
                return cv;
        }

        @Override
        public String[] getColumnNames() {
                return Database.Orderable.ALL_COLUMNS;
        }
}
