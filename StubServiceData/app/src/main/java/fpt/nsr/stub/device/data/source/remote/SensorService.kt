package fpt.nsr.stub.device.data.source.remote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import fpt.nsr.stub.device.domain.model.SensorModel
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class SensorService : Service(), SensorEventListener {
    companion object {
        const val KEY_ON_SENSOR_CHANGED_ACTION =
            "fpt.nsr.stub.device.data.source.remote.SensorService"
        const val MAX_COUNT = 1000

        private const val xIndex: Int = 0
        private const val yIndex: Int = 1
        private const val zIndex: Int = 2
    }

    var currentTime: Long = 0L
    val gyros: Queue<SensorModel> = LinkedList()
    val gravities: Queue<SensorModel> = LinkedList()
    val xQueue: Queue<Float> = LinkedList()
    val yQueue: Queue<Float> = LinkedList()
    private lateinit var sensorManager: SensorManager

    override fun onCreate() {
        super.onCreate()
        currentTime = System.currentTimeMillis()
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.also { gyro ->
            sensorManager.registerListener(
                this, gyro, 10000,
            )
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)?.also { gyro ->
            sensorManager.registerListener(
                this, gyro, 10000,
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }
        Log.d("time: ", System.currentTimeMillis().toString())
        val intent = Intent(KEY_ON_SENSOR_CHANGED_ACTION)
        var builder = StringBuilder()
        builder.apply {
            append("\t ")
            append(event.values[xIndex])
            append("\t ")
            append(event.values[yIndex])
            append("\t ")
            append(event.values[zIndex])
        }

        val sensorModel = SensorModel(
            event.values[xIndex].toString(),
            event.values[yIndex].toString(),
            event.values[zIndex].toString()
        )

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            intent.putExtra("Gyro", sensorModel)
            gyros.add(sensorModel)
        } else {
            // add a message to queue for creating a file
            gravities.add(sensorModel)
            xQueue.add(event.values[xIndex])
            yQueue.add(event.values[yIndex])
            //region Event handler
            //region Shock
            if (sqrt(event.values[xIndex].pow(2) + event.values[yIndex].pow(2)) >= 2) {
                intent.putExtra("Shock", true)
            }
            //endregion
            //region Brake
            if (xQueue.size >= 50) {
                var sumX: Float = 0F
                for (i in 1..50) {
                    sumX += xQueue.poll()
                }
                if (sumX / 50 >= 0.3) {
                    intent.putExtra("Brake", true)
                }
                if (sumX / 50 <= -0.4) {
                    intent.putExtra("Accel", true)
                }
            }
            //endregion
            //region Handling
            if (yQueue.size == 50) {
                var sumY = 0F
                for (i in 1..50) {
                    sumY += yQueue.poll()
                }
                if (abs(sumY / 50) >= 0.4) {
                    intent.putExtra("Handling", true)
                }
            }
            //endregion
        }
        writeToFile()
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    fun writeToFile() {
        if (gravities.size > MAX_COUNT && gyros.size > MAX_COUNT) {
            //Create Header
            val builder = StringBuilder()
            builder.apply {
                append("mstime")
                append("\t")
                append("g.x ")
                append("\t")
                append("g.y")
                append("\t")
                append("g.z")
                append("\t")
                append("gyro.x")
                append("\t")
                append("gyro.y")
                append("\t")
                append("gyro.z")
                append("\n")
            }
            //Create body
            for (i in 1..MAX_COUNT) {
                builder.apply {
                    append(currentTime)
                    append("\t")
                    append(gravities.poll().toString())
                    append("\t")
                    append(gyros.poll().toString())
                    append("\n")
                }
                currentTime += 10
            }
            applicationContext.openFileOutput("data.csv", Context.MODE_PRIVATE).use {
                it.write(builder.toString().toByteArray())
            }
        }

    }
}