package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SoundEffectConstants
import android.widget.Button

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val onlineButton : Button = findViewById(R.id.playOnline)
        val offlineButton : Button = findViewById(R.id.playOffline)
        //offlineButton.playSoundEffect(SoundEffectConstants.CLICK)
        offlineButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}