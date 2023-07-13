package com.example.helloworld

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Size
import android.view.View
import com.example.helloworld.src.GameField


class Draw2D(context: Context?, private var field: GameField) : View(context) {
    private val paint: Paint = Paint()
    private val blackPaint = Paint(Color.BLACK)
    private val bluePaint = Paint(Color.BLUE)
    private val redPaint = Paint(Color.RED)
    private val greyPaint = Paint(Color.GRAY)

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.apply {
            style = Paint.Style.FILL // стиль Заливка
            color = Color.WHITE // закрашиваем холст белым цветом
        }
        canvas?.drawPaint(paint)
        drawField(canvas, 50)
    }
    fun drawField( canvas: Canvas?, cellSize:Int) {
        // draw grid
        
        val xShift = getScreenWidth() / 2f - field.getWidth() * cellSize / 2f
        val yShift = getScreenHeight() / 2f - field.getHeight() * cellSize / 2f
        for(i in 0 .. field.getWidth()) {
            canvas?.drawLine(
                xShift + i * cellSize,
                yShift,
                xShift + i * cellSize,
                yShift + cellSize * field.getHeight(),
                blackPaint)
        }
        for(i in 0 .. field.getHeight()) {
            canvas?.drawLine(
                xShift,
                yShift + i * cellSize,
                xShift + cellSize * field.getWidth(),
                yShift + i * cellSize,
                blackPaint)
        }
        for(i in 0 until field.getHeight()) {
            for(j in 0 until field.getWidth()) {
             /*   // TODO()*/
            }
        }
    }
}