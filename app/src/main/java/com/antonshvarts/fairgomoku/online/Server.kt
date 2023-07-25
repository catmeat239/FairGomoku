package com.antonshvarts.fairgomoku.online

import android.util.Log
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Server(private var gameID:String, val myID : String, val opponentsID : String, val logic : GameLogic) {
    private var hrenForDataListener: Boolean = true
    private var opponentListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.d("Game", "Hren=: ${hrenForDataListener.toString()}")
            if (!hrenForDataListener) {

                val data = snapshot.getValue<TurnInfo>()
                Log.d("Game", "Data has arrived: ${data.toString()}")
                if (data != null) {
                    logic.redFigure = Pair(data.x, data.y)
                }
                if (logic.isDataSent) {
                    // have all info
                    //logic.isBluePlaying = false
                    logic.changeTurn()
                    logic.isDataSent = false
                } //else logic.isBluePlaying = true
            }else hrenForDataListener = false
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("Game", "Server:onCancelled", error.toException())
        }
    }
    private var turnNumb:Int = 0
    // WE ARE BLUE!!! WE ARE BLUE!!! WE ARE BLUE !!!
   // private var turnNumberTimesTwo :Int = 0
    private var database: DatabaseReference  = FirebaseDatabase.getInstance().reference
    .child("games").child(gameID)

    init {
       setListenerToData()
    }
    fun sendData(data : TurnInfo) {
        val newData = database.child(myID).child(turnNumb.toString())
        newData.setValue(data)
        if (!logic.isDataSent) {
            turnNumb++
            setListenerToData()
        }
        else{
            setListenerToData()
            turnNumb++
        }
        logic.isDataSent = true
        Log.d("Game","Data has sent: ${data.toString()}")
        Log.d("Game", "Can get the data ${newData}, Turn: $turnNumb")
    }

private fun setListenerToData(){
    hrenForDataListener = true
    Log.d("Game","My id:${myID}, Opponent id:${opponentsID}, turn: $turnNumb")
    //Thread.sleep(3000)
    database.child(opponentsID).child(turnNumb.toString()).addValueEventListener(opponentListener)
    //Thread.sleep(3000)
   // hrenForDataListener = false
}


}