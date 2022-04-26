package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.service.referencedata.ProgramService;

public class ProgramListViewModel extends AndroidViewModel {
    ProgramService programService;
    private MutableLiveData<String> programId;
    private MutableLiveData<String> homeFacilityId;
    public static final String KEY_HOME_FACILITY_ID = "homeFacilityId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_TOKEN_EXPIRATION = "expires_in";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";

    public ProgramListViewModel(@NonNull Application application) {

        super(application);
        programId = new MutableLiveData<String>();
        SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        getHomeFacilityId().setValue(sharedPrefs.getString(KEY_HOME_FACILITY_ID, ""));
    }

    public ArrayList<Program> getPrograms(){
        programService = new ProgramService(getApplication());
        return programService.getPrograms();
    }

    public MutableLiveData<String> getProgramId() {
        if(programId == null){
            programId = new MutableLiveData<String>();
        }
        return programId;
    }

    public MutableLiveData<String> getHomeFacilityId() {
        if(homeFacilityId == null){
            homeFacilityId = new MutableLiveData<String>();
        }
        return homeFacilityId;
    }
}
