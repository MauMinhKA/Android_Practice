@file:Suppress("UNREACHABLE_CODE")

package com.example.demofragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment1.*

class MyFragment1 : Fragment() {

    companion object {
        val TAG = MyFragment1::class.java.name
    }

    private var mainViewModel: MainViewModel? = null
    private lateinit var listener: CommunicationInterface
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunicationInterface) listener = context
        else throw  RuntimeException("$context must implement onViewSelected!");
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        btn_submit.setOnClickListener {
            mainViewModel?.sendDate(edt_name.text.toString(),
                edt_age.text.toString())
        }
    }
}