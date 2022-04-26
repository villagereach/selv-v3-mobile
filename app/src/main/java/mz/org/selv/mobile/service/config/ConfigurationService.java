package mz.org.selv.mobile.service.config;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import mz.org.selv.mobile.model.configuration.Server;
import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;

public class ConfigurationService {
    private Context mContext;

    public ConfigurationService(Context context) {
        this.mContext = context;
    }

    public String lastUsedService(){
        Database database = new Database(mContext);
        database.open();
        Date maxDate;
        Cursor cursor = database.select(Server.class, null, null, null, null,
                Database.Server.COLUMN_LAST_TIME_USED+" DESC");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Server server = Converter.cursorToServer(cursor);
            cursor.close();
            return server.getUrl();
        }
        database.close();
        return "";
    }
}
