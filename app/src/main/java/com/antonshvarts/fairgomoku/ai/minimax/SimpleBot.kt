package com.antonshvarts.fairgomoku.ai.minimax


import android.util.Log
import com.antonshvarts.fairgomoku.ai.Bot
import com.antonshvarts.fairgomoku.logic.Cell

class SimpleBot : Bot() {
    private var isMoveFirst: Boolean = true
    private var minimaxDepth = 3
    /**
     * @param field in GameLogic with state before move
     * @return move as Pair<Int,Int> searched by minimax algorithm
     */
    override fun move(field : Array<Array<Cell>>): Pair<Int, Int> {

        // Make the AI instance calculate a move.
        if (isMoveFirst) {
            isMoveFirst = false
            return Pair(field.size/2,field[0].size/2)
        }
        val ai = Minimax(fieldToboard(field))
        val aiMove: IntArray? = ai.calculateNextMove(minimaxDepth)
        if(aiMove == null){
            Log.d("Game","random move")
            return super.move(field)
        }
        return Pair(aiMove[0],aiMove[1])
    }

    private fun fieldToboard(field: Array<Array<Cell>>): Board {
        fun cellToInt(c:Cell):Int{
            return when(c){
                Cell.EMPTY ->  0
                Cell.BLUE  ->  1
                Cell.RED   ->  2
                Cell.GRAY  -> -1
            }
        }
        val board = Board(field.size,field[0].size)
        for(i in field.indices){
            for(j in 0 until field[0].size){
                board.boardMatrix[i][j] = cellToInt(field[i][j])
            }
        }
        return board
    }
    /**
     * @param i is depth level for search (equals 3 by default)
     */
    override fun setDifficulty(i: Int ) {
        minimaxDepth = i
    }
}