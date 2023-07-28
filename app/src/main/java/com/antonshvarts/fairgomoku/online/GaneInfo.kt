package com.antonshvarts.fairgomoku.online

data class RoomInfo(val id : String = "", val fieldSize : Pair<Int, Int> = Pair(15, 15), val timeForTurn : Int = 30) {

}
