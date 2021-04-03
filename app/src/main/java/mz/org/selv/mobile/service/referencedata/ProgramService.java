package mz.org.selv.mobile.service.referencedata;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.referencedata.Program;

public class ProgramService {
    Context mContext;
    public ProgramService(Context context){
        this.mContext = context;
    }

    public ArrayList getPrograms(){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Program.class, null, null, null, null, null);
        ArrayList <Program> programList =  new ArrayList<>();
        if(cursor != null &&  cursor.getCount() > 0){
            while (cursor.moveToNext()){
                programList.add(Converter.cursorToProgram(cursor));
            }
        }

        cursor.close();
        database.close();
        return programList;
    }
}
