package com.antonshvarts.fairgomoku.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.antonshvarts.fairgomoku.Draw2D
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.antonshvarts.fairgomoku.online.GameServer
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments:Bundle? = intent.extras
        val isOnline = arguments?.getBoolean("mode")
        val gameLogic : GameLogic = GameLogic(isOnline!!,)

        // setContent`View(R.layout.activity_main)
        var gameServer: GameServer? = null
        if(isOnline) {
            val myID = arguments.getString("myID")
            val opponentID = arguments.getString("opponentID")
            if (myID == null || opponentID == null)
                throw RuntimeException("ids null")

            gameServer = GameServer("3", myID, opponentID, gameLogic)
        }
        val draw2D = Draw2D(this, gameLogic, gameServer)

        setContentView(draw2D)
       // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //gameField.setCell(0,0,Cell.BLUE)
    }

    override fun onBackPressed() {
        val backToMenuDialogBuilder = AlertDialog.Builder(this)

        backToMenuDialogBuilder.setMessage("Are you sure?")
        backToMenuDialogBuilder.setPositiveButton("Yes") { _, _ -> {}
            super.onBackPressed()
        }
        backToMenuDialogBuilder.setNegativeButton("No") { _, _ -> {} }
        backToMenuDialogBuilder.setCancelable(true)
        backToMenuDialogBuilder.show()
    }

}