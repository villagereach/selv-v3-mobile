package mz.org.selv.mobile.service.referencedata;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.referencedata.TradeItem;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.ValidDestination;
import mz.org.selv.mobile.model.stockmanagement.ValidReasons;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;

public class ReferenceDataService {
    Context mContext;
    public ReferenceDataService(Context context){
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

    public Program getProgramById(String programId){
        Database database = new Database(mContext);
        database.open();
        Program program = new Program();
        Cursor cursor = database.select(Program.class, Database.Program.COLUMN_UUID+"=?", new String[]{programId}, null, null, null);
        if(cursor != null &&  cursor.getCount() > 0){
            if (cursor.moveToFirst()){
                program = Converter.cursorToProgram(cursor);
            }
        }

        cursor.close();
        database.close();
        return program;
    }

    public List getFacilityTypeApprovedProducts(String programId, String facilityTypeId){
        Database db = new Database(mContext);
        db.open();
        Cursor cursor = db.select(FacilityTypeApprovedProductAndProgram.class, Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_PROGRAM_ID+" = ? AND "+
                Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID+" = ?", new String[]{programId, facilityTypeId}, null, null, null);
        List result = new ArrayList<>();
        while(cursor.moveToNext()){
                result.add(Converter.cursorToFacilityTypeApprovedProduct(cursor));
        }
        cursor.close();
        db.close();
        return result;
    }

    public List getValidOrderables(String programId, String facilityTypeId){
        List<FacilityTypeApprovedProductAndProgram> ftap = getFacilityTypeApprovedProducts(programId, facilityTypeId);
        List orderableIdList = new ArrayList<Orderable>();
        Database database = new Database(mContext);
        database.open();
        for(int i = 0; i < ftap.size(); i++){
            Cursor cursor = database.select(Orderable.class, Database.Orderable.COLUMN_UUID+" = ?", new String[]{ftap.get(i).getOrderableId()}, null, null, null);

            while(cursor.moveToNext()){
                orderableIdList.add(Converter.cursorToOrderable(cursor));
            }
            cursor.close();
        }
        database.close();
        return orderableIdList;
    }

    public Orderable getOrderableByName(String orderableName){
        Orderable orderable = null;
        Database db = new Database(mContext);
        db.open();
        Cursor cursor = db.select(Orderable.class, Database.Orderable.COLUMN_NAME +" = ?", new String[]{orderableName}, null, null, null);
        if(cursor.moveToFirst()){
            orderable = Converter.cursorToOrderable(cursor);
        }
        cursor.close();
        db.close();
        return orderable;
    }

    public Orderable getOrderableById(String orderableId){
        Orderable orderable = null;
        Database db = new Database(mContext);
        db.open();
        Cursor cursor = db.select(Orderable.class, Database.Orderable.COLUMN_UUID +" = ?", new String[]{orderableId}, null, null, null);
        if(cursor.moveToFirst()){
            orderable = Converter.cursorToOrderable(cursor);
        }
        cursor.close();
        db.close();
        return orderable;
    }

    public List getLotsByOrderableName(String name){
        Orderable orderable = getOrderableByName(name);
        List<TradeItem> tradeItems = getTradeItemsByOrderable(orderable);
        String[] tradeItemsId = new String[tradeItems.size()];
        for (int i = 0; i < tradeItems.size(); i++){
            tradeItemsId[i] = tradeItems.get(i).getTradeItemId();
        }

        StringBuilder parameters = new StringBuilder();
        for(int i = 0; i < tradeItemsId.length; i++){
            parameters.append("?,");
        }
        List<Lot> lots = new ArrayList<>();
        Cursor cursor;
        Database db = new Database(mContext);
        db.open();
        if(orderable != null){
            cursor = db.select(Lot.class, Database.Lot.COLUMN_TRADE_ITEM_ID +" IN ("+parameters.deleteCharAt(parameters.length()-1).toString()+")", tradeItemsId, null, null, null);
            while(cursor.moveToNext()){
                lots.add(Converter.cursorToLot(cursor));
            }
            cursor.close();
        }

        db.close();
        return lots;
    }

    public List getTradeItemsByOrderable(Orderable orderable){
        Database db = new Database(mContext);
        db.open();
        List<TradeItem> tradeItems = new ArrayList<>();
        Cursor cursor = db.select(TradeItem.class, Database.TradeItem.COLUMN_ORDERABLE_ID +" = ?", new String[]{orderable.getUuid()}, null, null, null);
        while(cursor.moveToNext()){
            TradeItem ti = Converter.cursorToTradeItem(cursor);
            tradeItems.add(ti);
        }
        cursor.close();
        db.close();
        return tradeItems;
    }

    public List<ValidReasons> getValidReasons(String facilityTypeId, String programId){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(ValidReasons.class, Database.ValidReasons.COLUMN_NAME_FACILITY_TYPE_ID+"=? AND "+
                Database.ValidReasons.COLUMN_NAME_PROGRAM_ID+"=?", new String[]{facilityTypeId, programId}, null, null, null);
        List validReasons = new ArrayList<ValidReasons>();
        while (cursor.moveToNext()){
            validReasons.add(Converter.cursorToValidReasons(cursor));
        }
        cursor.close();
        database.close();
        return validReasons;
    }

    public List<ValidSource> getValidSources(String facilityTypeId, String programId){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(ValidSource.class, Database.ValidSources.COLUMN_NAME_FACILITY_TYPE_ID+"=? AND "+
                Database.ValidSources.COLUMN_NAME_PROGRAM_ID+"=?", new String[]{"113db84f-b0f8-4fec-9d37-ae87fcd833d7", programId}, null, null, null);
        List validSources = new ArrayList<ValidSource>();
        while (cursor.moveToNext()){
            validSources.add(Converter.cursorToValidSource(cursor));
        }
        cursor.close();
        database.close();
        return validSources;
    }

    public List<ValidDestination> getValidDestinations(String facilityTypeId, String programId){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(ValidDestination.class, Database.ValidSources.COLUMN_NAME_FACILITY_TYPE_ID+"=? AND "+
                Database.ValidDestinations.COLUMN_NAME_PROGRAM_ID+"=?", new String[]{"be01380b-4939-47a1-a5ce-72c691d63a8e", programId}, null, null, null);
        List validDestinations = new ArrayList<ValidSource>();
        while (cursor.moveToNext()){
            validDestinations.add(Converter.cursorToValidDestinations(cursor));
        }
        cursor.close();
        database.close();
        return validDestinations;
    }

    public Reason getReasonById(String reasonId){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Reason.class, Database.Reason.COLUMN_NAME_UUID +"=?", new String[]{reasonId}, null, null, null);
        Reason reason = new Reason();
        if (cursor.moveToFirst()){
            reason = Converter.cursorToReason(cursor);
        }
        cursor.close();
        database.close();
        return reason;
    }

    public Reason getReasonByName(String reasonName){
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Reason.class, Database.Reason.COLUMN_NAME_NAME +"=?", new String[]{reasonName}, null, null, null);
        Reason reason = new Reason();
        if (cursor.moveToFirst()){
            reason = Converter.cursorToReason(cursor);
        }
        cursor.close();
        database.close();
        return reason;
    }

    public List<String> getReasonNameByValidReason(String facilityTypeId, String programId, String category, String type){
        List reasonNames = new ArrayList<>();
        List<ValidReasons> validReasons = getValidReasons(facilityTypeId, programId);

        for(int i = 0; i < validReasons.size(); i++){
            if(category != null){
                Reason reason = getReasonById(validReasons.get(i).getReasonId());
                if(type != null){
                    if(reason.getCategory().equals(category) && reason.getType().equals(type)){
                        reasonNames.add(reason.getName());
                    }
                } else {
                    if(reason.getCategory().equals(category)){
                        reasonNames.add(reason.getName());
                    }
                }
            } else {
                if(type != null){
                    Reason reason = getReasonById(validReasons.get(i).getReasonId());
                    if(reason.getType().equals(type)){
                        reasonNames.add(reason.getName());
                    } else {
                        reasonNames.add(getReasonById(validReasons.get(i).getReasonId()).getName());
                    }
                } else {
                    reasonNames.add(getReasonById(validReasons.get(i).getReasonId()).getName());
                }
            }
        }
        return reasonNames;
    }

    public Lot getLotByCode(String code){
        Lot lot = new Lot();
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Lot.class, Database.Lot.COLUMN_CODE+"=?", new String[]{code}, null, null, null);
        if(cursor.moveToFirst()){
            lot = Converter.cursorToLot(cursor);
        }
        cursor.close();
        database.close();
        return lot;
    }

    public Lot getLotById(String lotId){
        Lot lot = new Lot();
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(Lot.class, Database.Lot.COLUMN_UUID+"=?", new String[]{lotId}, null, null, null);
        if(cursor.moveToFirst()){
            lot = Converter.cursorToLot(cursor);
        }
        cursor.close();
        database.close();
        return lot;
    }
}
