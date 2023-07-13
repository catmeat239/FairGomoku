package com.example.helloworld

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Layout
import com.example.helloworld.src.Cell

import com.example.helloworld.src.GameField

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameField : GameField = GameField(15, 15)
       // setContent`View(R.layout.activity_main)
        val draw2D = Draw2D(this, gameField)

        setContentView(draw2D)

        //gameField.setCell(0,0,Cell.BLUE)
    }
}