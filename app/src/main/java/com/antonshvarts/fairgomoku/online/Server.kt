package com.antonshvarts.fairgomoku.online

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Server(private var gameID:String, val myID : String, val opponentsID : String) {
    private var isDataSent = false

   // private var turnNumberTimesTwo :Int = 0
    private var database: DatabaseReference = Firebase.database.reference
        .child("games").child(gameID)
    init {
        val opponentListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<TurnInfo>()
                if(isDataSent) {

                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Game", "Server:onCancelled", error.toException())
            }
        }
        database.child(opponentsID).addValueEventListener(opponentListener)
    }
    fun sendData(data : TurnInfo) {
        database.child(myID).setValue(data)
        isDataSent = true
    }




}