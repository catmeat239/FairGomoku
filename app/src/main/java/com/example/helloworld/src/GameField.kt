package com.example.helloworld.src

import android.util.Log

class GameField (private val width : Int, private val height: Int) {
    init{
        if(width <= 0 || height <= 0)
            throw Exception("width <= 0 or height <= 0")
    }
    private val field : Array<Array<Cell>> = Array(height,) {Array(width) { Cell.EMPTY }}

    fun getCell(i : Int, j : Int) : Cell = field[i][j]
    fun setCell(i : Int, j : Int, cell : Cell) {
        Log.d("Game", "setCell: $i $j from ${field[i][j]} to $cell")
        field[i][j] = cell
    }
    fun getWidth() = width
    fun getHeight()= height


}