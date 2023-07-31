package com.antonshvarts.fairgomoku

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.antonshvarts.fairgomoku.activity.MainActivity
import com.antonshvarts.fairgomoku.logic.Cell
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.antonshvarts.fairgomoku.online.GameServer
import com.antonshvarts.fairgomoku.online.TurnInfo


class Draw2D(context: Context?, private val logic: GameLogic, val gameServer : GameServer?) : View(context),
    View.OnTouchListener {


    private var timeLeft: Long = 30
    val backButton = resources.getDrawable(R.drawable.back_button, null)
    private val paint = Paint()
    private val buttonRect  =  Rect(getScreenWidth() / 2 - 300, 1500, getScreenWidth() / 2 + 300, 1500 + 300)
    private val cellSize = 50
    val xShift = getScreenWidth() / 2f - logic.getFieldWidth() * cellSize / 2f
    val yShift = getScreenHeight() / 2f - logic.getFieldHeight() * cellSize / 2f

    init{
        logic.setReDraw(this)
        this.setOnTouchListener(this)
        backButton.dirtyBounds
        backButton.setBounds(0, 0, backButton.minimumWidth / 7, backButton.minimumHeight / 7)
    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.paint.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        canvas?.drawPaint(this.paint)
        drawPicture(canvas)
        drawLastMove(canvas)
        drawField(canvas)
        drawButton(canvas)
        drawText(canvas)
        drawTimer(canvas)
       // drawLastMove(canvas)
    }
    private fun drawLastMove(canvas: Canvas?) {
        if(logic.moves.size > 0) {
            paint.color = Color.GREEN
            canvas?.drawCircle(logic.moves.last().first.second * cellSize + xShift, logic.moves.last().first.first * cellSize + yShift, cellSize / 2.3f, paint)
            paint.color = Color.GREEN
            canvas?.drawCircle(logic.moves.last().second.second * cellSize + xShift, logic.moves.last().second.first * cellSize + yShift, cellSize / 2.3f, paint)

        }
        // paint.color = Color.

    }
    private fun drawTimer(canvas: Canvas?) {
        paint.textSize = 48F
        paint.color = Color.BLACK
        canvas?.drawText(timeLeft.toString(), 100f, 700f, paint)
    }

    private fun drawButton(canvas: Canvas?) {
        paint.color = Color.GREEN
        if(logic.figurePlacement == null || logic.blueFigure != null)
            paint.color = Color.LTGRAY
        canvas?.drawRect(buttonRect, paint)
    }

    private fun drawText(canvas: Canvas?) {
        paint.textSize = 48F
        Log.d("Game", "Who win in DrawText = ${logic.whoWin}")
        if(logic.whoWin != null) {
            paint.color = Color.MAGENTA
            canvas?.drawText("GAME OVER!!! ${logic.whoWin}", 100f, 500f, paint)
        } else {
            paint.color = if(logic.getTurn()) Color.BLUE else Color.RED
            if(gameServer == null)
                canvas?.drawText("YOUR TURN, ${if (logic.getTurn()) "BLUE" else "RED"}", 100f, 500f, paint)
        }

    }
    private fun drawPicture(canvas : Canvas?) {
        backButton.draw(canvas!!)
    }

    private fun drawField(canvas: Canvas?) {
        // draw grid
        this.paint.color = Color.BLACK
        // horizontal
        for(i in 0 .. logic.getFieldWidth()) {
            canvas?.drawLine(
                xShift + i * cellSize,
                yShift,
                xShift + i * cellSize,
                yShift + cellSize * (logic.getFieldHeight()),
                this.paint
            )
        }
        for(i in 0 .. logic.getFieldHeight()) {
            canvas?.drawLine(
                xShift,
                yShift + i * cellSize,
                xShift + cellSize * (logic.getFieldWidth()),
                yShift + i * cellSize,
                this.paint
            )
        }
        //drawLastMove(canvas)
        for(i in 0 .. logic.getFieldHeight()) {
            for(j in 0 .. logic.getFieldWidth()) {
               paint.color = when(logic.getCell(i, j)) {
                   Cell.BLUE -> Color.BLUE
                   Cell.RED -> Color.RED
                   Cell.GRAY -> Color.GRAY
                   else -> Color.WHITE
               }

                if(paint.color != Color.WHITE) {
                    canvas?.drawCircle(j * cellSize + xShift, i * cellSize + yShift, cellSize / 3f, paint)
                }
            }
        }

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if(p1 == null) return false
        val x = p1.getX().toInt()
        val y = p1.getY().toInt()

        Log.d("Game", "onTouch  ${x}  ${y} ")

        if(backButton.bounds.contains(x, y)) {
            val backToMenuDialogBuilder = AlertDialog.Builder(context)

            backToMenuDialogBuilder.setMessage("Are you sure?")
            backToMenuDialogBuilder.setPositiveButton("Yes") { _, _ -> {}
                (context as MainActivity).finish()
                //val intent = Intent(context, MenuActivity::class.java)
                //startActivity(intent)

            }
            backToMenuDialogBuilder.setNegativeButton("No") { _, _ -> {} }
            backToMenuDialogBuilder.setCancelable(true)
            backToMenuDialogBuilder.show()
            return false
         }
        if(logic.whoWin != null) {
            // TODO(WORKS ONLY BACK AND RETURN TO MENU BUTTON)
            return true
        }

        // todo(block the users touch if in online he already use his move)
        //button


        if(logic.figurePlacement != null && buttonRect.contains(x,y)) {
            if(logic.blueFigure == null) {
                logic.blueFigure = logic.figurePlacement
                logic.figurePlacement = null
                logic.isBluePlaying = false
                logic.cancelTimer()
                if(gameServer == null) {
                    if(logic.withTheComputer) {
                        logic.redFigure = logic.moveBot()
                        logic.changeTurn()
                    } else {
                        logic.setCell(logic.blueFigure!!, Cell.EMPTY)
                        logic.changeTimer()
                    }
                } else

                    if(!logic.isDataSent) {
                        gameServer.sendData(
                            TurnInfo(
                                this.logic.blueFigure!!.first,
                                logic.blueFigure!!.second
                            )
                        )
                        logic.isDataSent = true
                        if(logic.redFigure != null) {
                            logic.changeTurn()
                        }
                    }
            } else if(gameServer == null) {
                logic.redFigure = logic.figurePlacement
                logic.figurePlacement = null
                logic.isBluePlaying = true
                logic.changeTurn()
            }
            return true
        }
        //field
        if (Rect(
                xShift.toInt() - cellSize / 2,
                yShift.toInt() - cellSize / 2,
                (xShift+cellSize*logic.getFieldWidth()).toInt() + cellSize / 2,
                (yShift+cellSize*logic.getFieldHeight()).toInt() + cellSize / 2
            ).contains(x,y) && (gameServer == null || logic.blueFigure == null)) {
            val j = ((x - xShift) / cellSize + 0.5).toInt()
            val i = ((y - yShift) / cellSize + 0.5).toInt()

            if (logic.getCell(i,j) == Cell.EMPTY) {
                if(logic.figurePlacement != null) logic.setCell(logic.figurePlacement!!, Cell.EMPTY)
                logic.figurePlacement = Pair(i,j)
                if(gameServer == null) logic.setCell(i, j, if(logic.getTurn()) Cell.BLUE else Cell.RED)
                else logic.setCell(i, j, Cell.BLUE)
                // TODO send move to server (maybe not here)
            }
        }
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    fun updateTimer(t : Long) {
        timeLeft = t
        invalidate()
    }
}