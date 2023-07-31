package com.antonshvarts.fairgomoku.ai

import com.antonshvarts.fairgomoku.logic.Cell
/**
 * Base class for all AI bots,
 * has one public method
 */
// todo (make it abstract)
 class Bot {
    /**
     * @param field in GameLogic with state before move
     * @return random move as Pair<Int,Int>
     */
    open fun move(field : Array<Array<Cell>>) :Pair<Int,Int>{
        val width = field.size
        val height = field[0].size
        val moves = HashSet<Pair<Int,Int>>()
        for(i in (0 until width)){
            for (j in (0 until height)){
                if (field[i][j] == Cell.EMPTY){
                    moves.add(Pair(i,j))
                }
            }
        }
        return moves.random()
    }
}