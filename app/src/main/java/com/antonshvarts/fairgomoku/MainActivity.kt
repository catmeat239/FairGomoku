package com.antonshvarts.fairgomoku

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SoundEffectConstants
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.antonshvarts.fairgomoku.logic.GameLogic
import com.antonshvarts.fairgomoku.online.Server
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments:Bundle? = intent.extras
        val isOnline = arguments?.getBoolean("mode")
        val gameLogic : GameLogic = GameLogic(isOnline!!,15, 15,)

        // setContent`View(R.layout.activity_main)
        var server: Server? = null
        if(isOnline) {
            val myID = arguments.getString("myID")
            val opponentID = arguments.getString("opponentID")
            if (myID == null || opponentID == null)
                throw RuntimeException("ids null")

            server = Server("3", myID, opponentID, gameLogic)
        }
        val draw2D = Draw2D(this, gameLogic, server)

        setContentView(draw2D)

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