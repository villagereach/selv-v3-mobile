package mz.org.selv.mobile.ui.stockmanagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StockManagementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StockManagementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}