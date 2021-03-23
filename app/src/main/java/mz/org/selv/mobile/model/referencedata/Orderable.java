package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

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
                return null;
        }

        @Override
        public ContentValues getContentValues() {
                return null;
        }

        @Override
        public String[] getColumnNames() {
                return new String[0];
        }
}
