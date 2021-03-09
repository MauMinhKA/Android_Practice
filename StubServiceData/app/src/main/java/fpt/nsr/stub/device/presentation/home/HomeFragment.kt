package fpt.nsr.stub.device.presentation.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.data.source.remote.SensorService
import fpt.nsr.stub.device.databinding.FragmentHomeBinding
import fpt.nsr.stub.device.domain.model.SensorModel
import fpt.nsr.stub.device.presentation.main.MainActivityCallback
import fpt.nsr.stub.device.util.Constants.GYRO_CHANNEL
import fpt.nsr.stub.device.util.Constants.MESSAGE_CHANNEL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private var mCallback: MainActivityCallback? = null

    private val mViewModel: HomeViewModel by viewModels()
    private var isShowGyroData = true

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    private var currentDate = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityCallback) {
            mCallback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        mBinding.apply {
            viewModel = mViewModel
            lifecycleOwner = this@HomeFragment
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initService()
    }

    private fun initView() {
        mViewModel.onReceivedIntentData.observe(viewLifecycleOwner, Observer {
            it?.let {
                mBinding.toggleReceived.isChecked = it
            }
        })

        mBinding.btnEmergency.setOnClickListener {
            Toast.makeText(context, "Emergency clicked!", Toast.LENGTH_SHORT).show()
        }

        mBinding.buttonSend.setOnClickListener {
            Intent().also { intent ->
                intent.action = GYRO_CHANNEL
                currentDate = sdf.format(Date())
                intent.putExtra("Message", currentDate)
                activity?.sendBroadcast(intent)
            }
            Toast.makeText(context, "Event Sent!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initService() {
        activity?.registerReceiver(
            broadcastReceiver,
            IntentFilter(MESSAGE_CHANNEL)
        )

        activity?.let {
            it.startService(Intent(activity?.applicationContext, SensorService::class.java))
            LocalBroadcastManager.getInstance(it).registerReceiver(
                gyroBroadcastReceiver, IntentFilter(
                    SensorService.KEY_ON_SENSOR_CHANGED_ACTION
                )
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
        activity?.let {
            it.unregisterReceiver(broadcastReceiver)
            LocalBroadcastManager.getInstance(it).unregisterReceiver(gyroBroadcastReceiver)
        }
    }

    companion object {
        val FRAGMENT_NAME: String = this::class.java.name

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.getStringExtra("Message")
            if (message != null) {
                Log.d("HomeFragment", " onIntent data received $message.toString()")
                mViewModel.receiveMessage()
                isShowGyroData = true
            }
        }
    }

    private val gyroBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var gyro = intent.getSerializableExtra("Gyro")
            Log.d("HomeFragment", "on gyro data received ($gyro as SensorModel).toString()")
            if (isShowGyroData) {
                if (gyro != null) {
                    mViewModel.setGyroValue(gyro as SensorModel)
                    handleGyroData(gyro)
                }

                mViewModel.setShockValue(intent.getBooleanExtra("Shock", false))
                mViewModel.setBrakeValue(intent.getBooleanExtra("Brake", false))
                mViewModel.setAccelValue(intent.getBooleanExtra("Accel", false))
                mViewModel.setHandlingValue(intent.getBooleanExtra("Handling", false))
            }
        }
    }

    private fun handleGyroData(gyro: SensorModel) {
        when {
            abs(Math.toDegrees(gyro.x.toDouble())) > 100 -> sendIntent(
                " x = " + abs(
                    Math.toDegrees(
                        gyro.x.toDouble()
                    )
                )
            )
            abs(Math.toDegrees(gyro.y.toDouble())) > 100 -> sendIntent(
                " y = " + abs(
                    Math.toDegrees(
                        gyro.y.toDouble()
                    )
                )
            )
            abs(Math.toDegrees(gyro.z.toDouble())) > 100 -> sendIntent(
                " z = " + abs(
                    Math.toDegrees(
                        gyro.z.toDouble()
                    )
                )
            )
        }

    }

    private fun sendIntent(dataSend: String) {
        Intent().also { intent ->
            intent.action = GYRO_CHANNEL
            currentDate = sdf.format(Date())
            intent.putExtra("Message", currentDate + dataSend)
            activity?.sendBroadcast(intent)
        }
        Toast.makeText(context, "Event Sent with shock in $dataSend!", Toast.LENGTH_LONG).show()
        isShowGyroData = false
    }
}