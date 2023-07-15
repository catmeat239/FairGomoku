package com.example.helloworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.helloworld.src.Cell

import com.example.helloworld.src.GameLogic

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameLogic : GameLogic = GameLogic(4, 3)

       // setContent`View(R.layout.activity_main)
        val draw2D = Draw2D(this, gameLogic)
        setContentView(draw2D)

        //gameField.setCell(0,0,Cell.BLUE)
    }
}