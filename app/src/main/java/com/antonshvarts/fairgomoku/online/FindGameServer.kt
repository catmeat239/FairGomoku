package com.antonshvarts.fairgomoku.online

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.antonshvarts.fairgomoku.activity.MainActivity
import com.antonshvarts.fairgomoku.activity.MenuActivity
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random


class FindGameServer(val menuActivity: MenuActivity) {
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Players")
    private var foundGame = false
    val charPool : List<Char>  = ('a' .. 'z') + ('A' .. 'Z') + ('0'..'9')
    private var playerID = List(25) { charPool.random() }.joinToString ( "" )

    private var opponentListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
                if(foundGame) return // todo (find better way to exit)

                val data : String? = snapshot.getValue<String>()
                Log.d("Game", "FindGameServer::Data has arrived: ${data.toString()}")
                if (data != null) {
                    startGame(data)
                }
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("Game", "Server:onCancelled", error.toException())
        }
    }
    init {
        addPlayer()
        database.child("found").addValueEventListener(opponentListener)
    }
    private fun addPlayer() {
        var data : String? = null
        database.child("waiting").get().addOnSuccessListener {
            data = it.value?.toString()
            Log.i("Game", "findGameServer::firebase::Got value ${it.value}")
            Log.d("Game", "findGameServer::data : $data")

            if(data != null) {
                database.child("found").setValue(playerID)
                startGame(data!!)
            }  else {
                Log.d("Game","addPlayer: playerId:$playerID")
                database.child("waiting").setValue(playerID)
            }
        }.addOnFailureListener{
            Log.e("Game", "findGameServer::firebase::Error getting data", it)
        }

    }
    private fun startGame(opponentID : String) {
        Log.d("Game", "findGameServer::StartGame")
        database.child("found").removeEventListener(opponentListener)
        database.child("found").removeValue()
        database.child("waiting").removeValue()
        foundGame = true
        menuActivity.startOnlineGame(playerID, opponentID)
    }
}