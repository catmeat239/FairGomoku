package com.example.helloworld.src

import android.graphics.Rect
import android.util.Log



class GameLogic (private val width : Int = 15, private val height: Int = 15, private val winSize : Int = 4,) {

    init{
        if(width <= 0 || height <= 0)
            throw Exception("width <= 0 or height <= 0")
    }
    private val field : Array<Array<Cell>> = Array(height) {Array(width,) { Cell.EMPTY }}
    private var isBluePlaying = true

    private var emptyCells = width * height
    var figurePlacement : Pair<Int, Int>? = null
    var blueFigure : Pair<Int, Int>? = null
    var whoWin : String? = null
    fun getCell(i : Int, j : Int) : Cell = field[i][j]
    // i -> Oy
    // j -> Ox
    fun setCell(p : Pair<Int, Int>, cell : Cell) {
        setCell(p.first, p.second, cell)
    }
    fun setCell(i : Int, j : Int, cell : Cell) {
        Log.d("Game", "setCell: $i $j from ${field[i][j]} to $cell")
        field[i][j] = cell
    }

    fun getFieldWidth() = width - 1
    fun getFieldHeight()= height - 1
    fun getTurn() = isBluePlaying
    private fun countCnt(i : Int, j : Int, delta : Pair<Int, Int>) : Int {
        var cnt = 0

        while(Rect(0,0,field.size, field[0].size).contains(i + cnt * delta.first, j + cnt * delta.second)
            &&  field[i + cnt * delta.first][j + cnt * delta.second] == field[i][j]) {
            cnt++

        }
        Log.d("Game", "countCnt returned $cnt")
        return cnt - 1
    }
    fun checkWin(p : Pair<Int, Int>) : Boolean {
        var i = p.first
        var j = p.second

        return countCnt(i, j, Pair(0, -1)) + 1 + countCnt(i, j, Pair(0, 1))  >= winSize ||
                countCnt(i, j, Pair(1, 0)) + 1 + countCnt(i, j, Pair(-1, 0))  >= winSize ||
                countCnt(i, j, Pair(1, 1)) + 1 + countCnt(i, j, Pair(-1, -1))  >= winSize ||
                countCnt(i, j, Pair(1, -1)) + 1 + countCnt(i, j, Pair(-1, 1))  >= winSize

    }

    fun changeTurn() {
        // do not forget to check if somebody has won
        if(isBluePlaying) {
            blueFigure = figurePlacement
            figurePlacement = null
            this.setCell(blueFigure!!, Cell.EMPTY)
        } else {
            if(blueFigure?.first == figurePlacement?.first && blueFigure?.second == figurePlacement?.second) {
                setCell(blueFigure!!, Cell.GRAY)
                emptyCells--
            } else {
                setCell(blueFigure!!, Cell.BLUE)
                setCell(figurePlacement!!, Cell.RED)
                emptyCells -= 2
                if (checkWin(blueFigure!!)) {
                    if(checkWin(figurePlacement!!)) {
                        whoWin = "TIE"
                        // tie
                    } else {
                        whoWin = "BLUE WON"
                        // blue
                    }
                } else {
                    if(checkWin(figurePlacement!!)) {
                        whoWin = "RED WON"
                        // red
                    }

                }

            }
            if(whoWin == null && emptyCells <= 0)
                whoWin = "TIE"
            Log.d("Game", "Who win in changeTurn = ${whoWin}")
            blueFigure = null
            figurePlacement = null
        }
        Log.d("Game", "emptySells = $emptyCells")
        isBluePlaying = !isBluePlaying
    }

}