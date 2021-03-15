package com.example.demo_singgleton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.demo_singgleton.modle.BubbleWrap
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class PopActivity : AppCompatActivity() {
    private lateinit var bubbleWrap: BubbleWrap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop)
//        bubbleWrap = BubbleWrap()
        setupPopButton()
        updateUI()

    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun setupPopButton() {
        btn_add.setOnClickListener(View.OnClickListener {
            BubbleWrap.popBubbles()
            updateUI()
        })
    }


    private fun updateUI() {
        val msg = String.format(
            Locale.getDefault(),
            "%d bubbles left ",
            BubbleWrap.numBubbles
        )
        txt_demo.text = msg
    }
}