package com.example.bottommenu_kotlin



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.example.bottommenu_kotlin.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val message = ObservableField<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    message.set("Home")
                    true
                }
                R.id.menu_notification -> {
                    message.set("Notification")
                    true
                }
                R.id.menu_search -> {
                    message.set("Search")
                    true
                }
                R.id.menu_profile -> {
                    message.set("Profile")
                    true
                }
                else -> false
            }
        }
    }
}