package fpt.nsr.stub.device.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var mBinding: FragmentMainBinding
    private val fragmentViewModel: MainFragmentViewModel by viewModels()
    private var mCallback: MainActivityCallback? = null

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
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        mBinding?.apply {
            mainViewModel = fragmentViewModel
        }

        return mBinding.root
    }

    companion object {
        val FRAGMENT_NAME: String = MainFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}