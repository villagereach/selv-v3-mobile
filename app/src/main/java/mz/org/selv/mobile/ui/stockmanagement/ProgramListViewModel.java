package mz.org.selv.mobile.ui.stockmanagement;

import android.app.Application;

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

    public ProgramListViewModel(@NonNull Application application) {

        super(application);
        programId = new MutableLiveData<String>();
    }

    public ArrayList<Program> getPrograms(){
        programService = new ProgramService(getApplication());
        return programService.getPrograms();
    }

    public MutableLiveData<String> getProgramId() {
        if(programId == null){
            programId = new MutableLiveData<String>();
            System.out.println("New Live Data");
        }
        return programId;
    }
}
