package com.example.demofragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment2.*

class MyFragment2 : Fragment() {

    companion object {
        val TAG = MyFragment2::class.java.name
    }

    private var mainViewModel: MainViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel?.name?.observe(viewLifecycleOwner, Observer {
            txt_name.text = it
        })
        mainViewModel?.age?.observe(viewLifecycleOwner, Observer {
            txt_age.text = it
        })
    }

    fun showInfo(name: String, age: String) {
        Log.d("Check", "showInfo: $name, $age")
        txt_name?.text = name
        txt_age.text = age
    }
}
