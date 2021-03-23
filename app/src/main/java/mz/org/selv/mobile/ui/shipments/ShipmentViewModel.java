package mz.org.selv.mobile.ui.shipments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShipmentViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ShipmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
