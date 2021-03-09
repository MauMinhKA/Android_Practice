package fpt.nsr.stub.device.presentation.main.userinfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fpt.nsr.stub.device.R
import fpt.nsr.stub.device.databinding.FragmentShowinfoBinding
import fpt.nsr.stub.device.presentation.main.MainActivityCallback

class FragmentShowInfo : Fragment() {
    private lateinit var mBindings: FragmentShowinfoBinding
    private val userViewModel: UserInfoViewModel by viewModels()
    private var mCallback: MainActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivityCallback){
            mCallback = context
        }
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mBindings =DataBindingUtil.inflate(inflater,R.layout.fragment_showinfo, container, false)
        mBindings.apply {
            userInfoViewModel = userViewModel
            lifecycleOwner = this@FragmentShowInfo
        }
        return mBindings.root
    }


}