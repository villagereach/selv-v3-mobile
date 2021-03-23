package mz.org.selv.mobile.ui.stockmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import mz.org.selv.mobile.model.referencedata.Program;

public class ProgramListViewModel extends ViewModel {
    public ProgramListViewModel() {

    }

    public ArrayList<Program> getPrograms(){
        Program program = new Program();
        program.setName("Name");
        program.setCode("Code");
        ArrayList<Program> programs = new ArrayList<>();
        programs.add(program);
        return programs;
    }

}
