package com.antonshvarts.fairgomoku.logic

import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import com.antonshvarts.fairgomoku.Draw2D


class GameLogic (
    private val isOnline : Boolean = false,
    private val width : Int = 15,
    private val height: Int = 15,
    private val winSize : Int = 4,
    private val timeForTern : Long = 30000L) {
    lateinit private var timer: CountDownTimer
    var isDataSent: Boolean =false
    private lateinit var draw2D :Draw2D

    init{
        timer = createTimer()
        timer.start()
        Log.d("Game"," User has set ${if(isOnline) "online" else "offline"} mode")
        if(width <= 0 || height <= 0)
            throw Exception("width <= 0 or height <= 0")
    }
    private val field : Array<Array<Cell>> = Array(height) {Array(width,) { Cell.EMPTY }}
    var isBluePlaying = true

   // private var timer = CountDownTimer()
    private var emptyCells = width * height

    var redFigure : Pair<Int, Int>? = null // second if online
    var blueFigure : Pair<Int, Int>? = null // first if online
    var figurePlacement : Pair<Int, Int>? = null
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

    fun checkWin(p : Pair<Int, Int>) : Boolean {

        fun countCnt(i : Int, j : Int, delta : Pair<Int, Int>) : Int {
            var cnt = 0

            while(Rect(0,0,field.size, field[0].size).contains(i + cnt * delta.first, j + cnt * delta.second)
                &&  field[i + cnt * delta.first][j + cnt * delta.second] == field[i][j]) {
                cnt++

            }
            //Log.d("Game", "countCnt returned $cnt")
            return cnt - 1
        }
        var i = p.first
        var j = p.second

        return countCnt(i, j, Pair(0, -1)) + 1 + countCnt(i, j, Pair(0, 1))  >= winSize ||
                countCnt(i, j, Pair(1, 0)) + 1 + countCnt(i, j, Pair(-1, 0))  >= winSize ||
                countCnt(i, j, Pair(1, 1)) + 1 + countCnt(i, j, Pair(-1, -1))  >= winSize ||
                countCnt(i, j, Pair(1, -1)) + 1 + countCnt(i, j, Pair(-1, 1))  >= winSize

    }

    fun changeTurn() {
        // do not forget to check if somebody has won

        if(blueFigure?.first == redFigure?.first && blueFigure?.second == redFigure?.second) {
            setCell(blueFigure!!, Cell.GRAY)
            emptyCells--
        } else {
            setCell(blueFigure!!, Cell.BLUE)
            setCell(redFigure!!, Cell.RED)
            emptyCells -= 2
            if (checkWin(blueFigure!!)) {
                if(checkWin(redFigure!!)) {
                    whoWin = "TIE"
                    // tie
                } else {
                    whoWin = "BLUE WON"
                    // blue
                }
            } else {
                if(checkWin(redFigure!!)) {
                    whoWin = "RED WON"
                    // red
                }

            }

        }
        if(whoWin == null && emptyCells <= 0)
            whoWin = "TIE"
        Log.d("Game", "Who win in changeTurn = $whoWin")

        blueFigure = null
        redFigure = null
        figurePlacement = null
        draw2D.invalidate()
        if(isOnline) {
            isDataSent = false
            draw2D.server!!.turnEnded()
        }


        changeTimer()
    }
    fun changeTimer() {
        timer.cancel()
        timer = createTimer().start()
    }
    fun createTimer(): CountDownTimer {
        return object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                draw2D.updateTimer(millisUntilFinished/1000)

            }

            override fun onFinish() {
                if(isOnline)
                    whoWin = "RED WON"
                else whoWin = if(isBluePlaying) "RED WON" else "BLUE WON"
                draw2D.invalidate()
            }
        }
    }

    fun setReDraw(draw2D: Draw2D) {
        this.draw2D = draw2D
    }


}