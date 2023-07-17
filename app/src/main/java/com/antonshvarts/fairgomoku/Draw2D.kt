package com.antonshvarts.fairgomoku

import android.R.attr
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.antonshvarts.fairgomoku.logic.Cell
import com.antonshvarts.fairgomoku.logic.GameLogic


class Draw2D(context: Context?, private var logic: GameLogic) : View(context),
    View.OnTouchListener {



    val d = resources.getDrawable(R.drawable.bred, null)
    private val paint = Paint()
    private val buttonRect  =  Rect(700 - 300, 1500, 700 + 300, 1500 + 300)
    private val cellSize = 50
    val xShift = getScreenWidth() / 2f - logic.getFieldWidth() * cellSize / 2f
    val yShift = getScreenHeight() / 2f - logic.getFieldHeight() * cellSize / 2f

    init{

        this.setOnTouchListener(this)
        d.setBounds(0, 0, 100, 500)
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.paint.apply {
            style = Paint.Style.FILL // стиль Заливка
            color = Color.WHITE // закрашиваем холст белым цветом
        }
        canvas?.drawPaint(this.paint)

        drawPicture(canvas)
        drawField(canvas)
        drawButton(canvas)
        drawText(canvas)
    }
    fun drawButton(canvas: Canvas?) {
        paint.color = Color.GREEN
        if(logic.figurePlacement == null)
            paint.color = Color.LTGRAY
        canvas?.drawRect(buttonRect, paint)
    }

    fun drawText(canvas: Canvas?) {
        paint.textSize = 48F
        Log.d("Game", "Who win in DrawText = ${logic.whoWin}")
        if(logic.whoWin != null) {
            paint.color = Color.MAGENTA
            canvas?.drawText("GAME OVER!!! ${logic.whoWin}", 100f, 500f, paint)
        } else {
            paint.color = if(logic.getTurn()) Color.BLUE else Color.RED
            canvas?.drawText("proghu proshenia zra bakaknul ${if (logic.getTurn()) "BLUE" else "RED"}", 100f, 500f, paint)
        }

    }
    fun drawPicture(canvas : Canvas?) {
        d.draw(canvas!!)
    }

    fun drawField( canvas: Canvas?) {
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
        if(logic.whoWin != null) {
            // TODO(WORKS ONLY BACK AND RETURN TO MENU BUTTON)
            return true
        }

        //button
        if(logic.figurePlacement != null && buttonRect.contains(x,y)) {
            logic.changeTurn()
        }
        //field
        if (Rect(
                xShift.toInt() - cellSize / 2,
                yShift.toInt() - cellSize / 2,
                (xShift+cellSize*logic.getFieldWidth()).toInt() + cellSize / 2,
                (yShift+cellSize*logic.getFieldHeight()).toInt() + cellSize / 2
            ).contains(x,y)){
            val j = ((x - xShift) / cellSize +0.5).toInt()
            val i = ((y - yShift) / cellSize +0.5).toInt()
            if(logic.getCell(i,j)==Cell.EMPTY){
                if(logic.figurePlacement != null) {
                    logic.setCell(
                        logic.figurePlacement!!.first,
                        logic.figurePlacement!!.second,
                        Cell.EMPTY
                    )
                }
                logic.figurePlacement = Pair(i,j)
                logic.setCell(i,j,if (logic.getTurn()) Cell.BLUE else Cell.RED )
            }
        }
        invalidate()
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}