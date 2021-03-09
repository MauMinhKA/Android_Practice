package fpt.nsr.stub.device.presentation.main

import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter

interface MainActivityCallback {
    fun onUpdateBottomNavigationData(list: List<BottomNavigationAdapter.BottomNavigationModel>)
}