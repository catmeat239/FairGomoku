package com.antonshvarts.fairgomoku.logic

import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import com.antonshvarts.fairgomoku.Draw2D
import com.antonshvarts.fairgomoku.ai.Bot
import com.antonshvarts.fairgomoku.ai.minimax.SimpleBot


class GameLogic (
    private val isOnline : Boolean = false,
    private val width : Int = 15,
    private val height: Int = 15,
    private val winSize : Int = 5,
    private val timeForTern : Long = 30000L) {
    private var timer: CountDownTimer
    var isDataSent: Boolean = false
    private lateinit var draw2D :Draw2D
    var bot : Bot = SimpleBot()

    init{
        bot.setDifficulty(4)
        timer = createTimer()
        timer.start()
        Log.d("Game"," User has set ${if(isOnline) "online" else "offline"} mode")
        if(width <= 0 || height <= 0)
            throw Exception("width <= 0 or height <= 0")
    }
    private val field : Array<Array<Cell>> = Array(height) {Array(width,) { Cell.EMPTY }}
    var isBluePlaying = true
    var withTheComputer = false
   // private var timer = CountDownTimer()
    private var emptyCells = width * height

    var moves : MutableList<Pair<Pair<Int,Int>, Pair<Int,Int>>> = MutableList(0)  { Pair(Pair(0, 0), Pair(0, 0)) }

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
    fun addMove() {

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
        moves.add(Pair(blueFigure!!, redFigure!!))
        if(blueFigure?.first == redFigure?.first && blueFigure?.second == redFigure?.second) {
            setCell(blueFigure!!, Cell.GRAY)
            emptyCells--
        } else {
            setCell(blueFigure!!, Cell.BLUE)
            setCell(redFigure!!, Cell.RED)
            emptyCells -= 2
            if (checkWin(blueFigure!!)) {
                if(checkWin(redFigure!!)) {
                    whoWin = "tie"
                    // tie
                } else {
                    whoWin = "blue"
                    // blue
                }
            } else {
                if(checkWin(redFigure!!)) {
                    whoWin = "red"
                    // red
                }

            }

        }
        if(whoWin == null && emptyCells <= 0)
            whoWin = "tie"
        Log.d("Game", "Who win in changeTurn = $whoWin")
        if(whoWin != null) {
            endGame()
        } else {
            blueFigure = null
            redFigure = null
            figurePlacement = null
            draw2D.invalidate()
            if(isOnline) {
                isDataSent = false
                draw2D.gameServer!!.turnEnded()
            }

            changeTimer()
        }
        isBluePlaying = true
    }
    fun endGame() {
        if(isOnline)
            draw2D.gameServer!!.sendWin(whoWin!!)
        timer.cancel()
        draw2D.invalidate()
    }
    fun cancelTimer() {
        timer.cancel()
    }
    fun changeTimer() {
        timer.cancel()
        timer = createTimer().start()
    }
    fun createTimer(): CountDownTimer {
        return object: CountDownTimer(timeForTern, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                draw2D.updateTimer(millisUntilFinished/1000)

            }

            override fun onFinish() {
                if(isOnline)
                    whoWin = "red"
                else whoWin = if(isBluePlaying) "red" else "blue"
                draw2D.invalidate()
            }
        }
    }

    fun setReDraw(draw2D: Draw2D) {
        this.draw2D = draw2D
    }

    fun moveBot(): Pair<Int, Int> {
        return bot.move(field)
    }


}