package mz.org.selv.mobile.ui.requisition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RequisitionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RequisitionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}