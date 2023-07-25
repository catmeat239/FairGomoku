package com.antonshvarts.fairgomoku.online

class TurnInfo() {
    // todo add time
    constructor(newX:Int, newY:Int) : this() {
        x = newX
        y = newY
    }
    var x : Int = -1
        set(value) {
            field = value
        }
        get() = field
    var y : Int = -1
        set(value) {
            field = value
        }
        get() = field

    override fun toString(): String {
        return "TurnInfo(x=$x, y=$y)"
    }

}