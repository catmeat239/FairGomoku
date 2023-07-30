package com.antonshvarts.fairgomoku.online

import android.util.Log
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class GameServer(private var gameID:String, val myID : String, val opponentsID : String, val logic : GameLogic) {
    private var hrenForDataListener: Boolean = true
    private var opponentListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.d("Game", "Hren=: ${hrenForDataListener.toString()}")
            if (!hrenForDataListener) {
                val data = snapshot.getValue<TurnInfo>()
                Log.d("Game", "Data has arrived: ${data.toString()} turn: ${turnNumb}")
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
    private var winListener : ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {


                val data = snapshot.getValue<String>()
                Log.d("Game", "winData has arrived: ${data.toString()}")
                if (data != null) {
                    logic.whoWin = if(data == "$myID") "blue"
                    else if(data == "$opponentsID")
                        "red"
                    else
                        "tie"
                }


        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("Game", "Server::winListener::onCancelled", error.toException())
        }
    }
    private var activePlayerListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            if (!hrenForDataListener) {
                val data = snapshot.getValue<TurnInfo>()
                Log.d("Game", "Data has arrived: ${data.toString()} turn: ${turnNumb}")
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

    private var databaseGame: DatabaseReference = FirebaseDatabase.getInstance().reference
    .child("games").child(gameID)

    init {
       setListenerToData()
    }
    fun sendData(data : TurnInfo) {
        val newData = databaseGame.child(myID).child(turnNumb.toString())
        newData.setValue(data)

        logic.isDataSent = true
        Log.d("Game","Data has sent: ${data.toString()}")

    }
    fun sendWin(whoWin : String) {
        val newData = databaseGame.child("whoWin")
        val data : String =
        if (whoWin == "blue") "$myID"
        else if(whoWin == "red")
            "$opponentsID"
        else
            "tie"
        newData.setValue(data)
        Log.d("Game","Data has sent: ${whoWin.toString()}")
    }
    fun turnEnded() {
        turnNumb++
        setListenerToData()
    }


private fun setListenerToData(){
    hrenForDataListener = true
    Log.d("Game","My id:${myID}, Opponent id:${opponentsID}, turn: $turnNumb")
    //Thread.sleep(3000)
    databaseGame.child(opponentsID).child(turnNumb.toString()).addValueEventListener(opponentListener)
    //Thread.sleep(3000)
   // hrenForDataListener = false
}


}