package fpt.nsr.stub.device.domain.model

import java.io.Serializable

data class SensorModel(
    var x: String,
    var y: String,
    var z: String
) : Serializable {
    override fun toString(): String {
        return x + "\t" + y + "\t" + z
    }
}