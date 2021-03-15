package com.example.demo_singgleton

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_singgleton.modle.BubbleWrap
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bubbleWrap: BubbleWrap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        bubbleWrap = BubbleWrap()
        setupAddMoreButton()
        setupPopActivateButton()
    }
    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun setupAddMoreButton() {
        btn_add.setOnClickListener(View.OnClickListener {
            BubbleWrap.addMoreBubbles()
            updateUI()
        })
    }

    private fun setupPopActivateButton() {
        btn_popping.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, PopActivity::class.java)
            startActivity(intent)
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