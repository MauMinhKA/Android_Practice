package fpt.nsr.stub.device.presentation.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fpt.nsr.stub.device.domain.model.SensorModel

class HomeViewModel @ViewModelInject constructor() :
    ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName
    var onReceivedIntentData = MutableLiveData<Boolean>()

    init {
        onReceivedIntentData.value = false
    }

    private val _gyro = MutableLiveData<String>()
    val gyro: LiveData<String>
        get() = _gyro

    private val _shock = MutableLiveData<Boolean>()
    val shock: LiveData<Boolean>
        get() = _shock

    private val _brake = MutableLiveData<Boolean>()
    val brake: LiveData<Boolean>
        get() = _brake

    private val _accel = MutableLiveData<Boolean>()
    val accel: LiveData<Boolean>
        get() = _accel

    private val _handling = MutableLiveData<Boolean>()
    val handling: LiveData<Boolean>
        get() = _handling

    fun setGyroValue(model: SensorModel?) {
        if (model != null) {
            _gyro.value = "x: " + model.x +
                    " y: " + model.y +
                    " z: " + model.z
        }
    }

    fun setShockValue(value: Boolean) {
        _shock.value = value
    }

    fun setBrakeValue(value: Boolean) {
        _brake.value = value
    }

    fun setAccelValue(value: Boolean) {
        _accel.value = value
    }

    fun setHandlingValue(value: Boolean) {
        _handling.value = value
    }

    fun receiveMessage() {
        onReceivedIntentData.value = !onReceivedIntentData.value!!
    }
}