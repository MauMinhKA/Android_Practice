package fpt.nsr.stub.device.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter.BottomNavigationModel
import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter.DATA_TYPE

class MainActivityViewModel @ViewModelInject constructor() :
    ViewModel() {

    private val TAG = MainActivityViewModel::class.java.simpleName
    val bottomNavigationLiveData = MutableLiveData<List<BottomNavigationModel>>()

    init {
        initBottomNavigationData()
    }

    private fun initBottomNavigationData() {
        val item1 = BottomNavigationModel(
            DATA_TYPE.MENU, "", R.drawable.ic_menu
        )
        val item2 = BottomNavigationModel(
            DATA_TYPE.VIDEO, "", R.drawable.ic_videocam
        )
        val item3 = BottomNavigationModel(
            DATA_TYPE.CAR, "", R.drawable.ic_car
        )
        val item4 = BottomNavigationModel(
            DATA_TYPE.OK, "OK", 0
        )
        bottomNavigationLiveData.value = listOf(item1, item2, item3, item4)
    }
}