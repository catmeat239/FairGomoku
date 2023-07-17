package com.example.helloworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SoundEffectConstants
import android.widget.Button
import com.example.helloworld.src.Cell

import com.example.helloworld.src.GameLogic

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameLogic : GameLogic = GameLogic(15, 15)

       // setContent`View(R.layout.activity_main)
        val draw2D = Draw2D(this, gameLogic)
        setContentView(draw2D)
        //gameField.setCell(0,0,Cell.BLUE)
    }
}