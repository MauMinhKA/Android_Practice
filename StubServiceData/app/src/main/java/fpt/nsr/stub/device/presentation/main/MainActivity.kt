package fpt.nsr.stub.device.presentation.main

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.databinding.ActivityMainBinding
import fpt.nsr.stub.device.presentation.home.HomeFragment
import fpt.nsr.stub.device.presentation.main.userinfo.FragmentShowInfo
import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter
import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter.BottomNavigationModel
import fpt.nsr.stub.device.presentation.navigation.BottomNavigationAdapter.DATA_TYPE
import fpt.nsr.stub.device.presentation.navigation.IBottomNavigationAdapterListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityCallback, IBottomNavigationAdapterListener {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var mBinding: ActivityMainBinding
    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null

    private var bottomNavigationAdapter: BottomNavigationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mBinding.mainViewModel = viewModel

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        if (savedInstanceState == null) initFirstPage()
        if (intent != null) processIntent(intent)

        this.bottomNavigationAdapter = BottomNavigationAdapter(this)
        mBinding.bottomNavigationRecyclerView.apply {
            adapter = bottomNavigationAdapter
        }

        viewModel.bottomNavigationLiveData.observe(this, Observer {
            it?.let {
                updateDataBottomNavigation(it)
            }
        })
    }

    private fun processIntent(checkIntent: Intent) {
        if (checkIntent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            Toast.makeText(this, "NFC data loaded!", Toast.LENGTH_SHORT).show()
            goToHomePage()
        }
    }

    private fun initFirstPage() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(
                R.id.container,
                MainFragment.newInstance(),
                MainFragment.FRAGMENT_NAME
            )
            .commitAllowingStateLoss()
    }

    private fun goToHomePage() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(
                R.id.container,
                HomeFragment.newInstance(),
                HomeFragment.FRAGMENT_NAME
            )
            .addToBackStack(HomeFragment.FRAGMENT_NAME)
            .commitAllowingStateLoss()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null)
            processIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null);
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this);
    }

    private fun updateDataBottomNavigation(list: List<BottomNavigationModel>) {
        list.let { bottomNavigationAdapter?.addData(it) }
    }

    //    override fun onBottomNavigationItemClick(bottomNavigationModel: BottomNavigationModel) {
//        when (bottomNavigationModel.type) {
//            DATA_TYPE.MENU -> Log.d("MainActivity", "MENU clicked")
//            DATA_TYPE.VIDEO -> Log.d("MainActivity", "VIDEO clicked")
//            DATA_TYPE.CAR -> Log.d("MainActivity", "CAR clicked")
//            DATA_TYPE.OK -> Log.d("MainActivity", "${bottomNavigationModel.text} clicked")
//        }
//    }
    override fun onBottomNavigationItemClick(bottomNavigationModel: BottomNavigationModel) {
        when (bottomNavigationModel.type) {
            DATA_TYPE.MENU -> Log.d("MainActivity", "MENU clicked")
            DATA_TYPE.VIDEO -> Log.d("MainActivity", "VIDEO clicked")
            DATA_TYPE.CAR -> Log.d("MainActivity", "CAR clicked")
            DATA_TYPE.OK -> homeScreen()
        }
    }

    private fun homeScreen() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.container, FragmentShowInfo())
            .addToBackStack(HomeFragment.FRAGMENT_NAME)
            .commitAllowingStateLoss()
    }

    override fun onUpdateBottomNavigationData(list: List<BottomNavigationModel>) {
        updateDataBottomNavigation(list)
    }
}
