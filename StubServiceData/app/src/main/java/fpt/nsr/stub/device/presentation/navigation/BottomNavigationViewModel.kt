package fpt.nsr.stub.device.presentation.navigation

import androidx.lifecycle.MutableLiveData

class BottomNavigationViewModel(val data: BottomNavigationAdapter.BottomNavigationModel) {

    private val TAG = BottomNavigationViewModel::class.java.simpleName
    private val navigationData = MutableLiveData<BottomNavigationAdapter.BottomNavigationModel>()

    init {
        navigationData.value = data
    }
}