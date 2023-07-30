package com.antonshvarts.fairgomoku.activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.antonshvarts.fairgomoku.R
import com.antonshvarts.fairgomoku.online.FindGameServer

class MenuActivity : AppCompatActivity() {
     lateinit var dialog :Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = Dialog(this)
        dialog.setTitle("Set game parametres")
        dialog.setContentView(R.layout.settings)
        var startOnlineGame = dialog.findViewById<Button>(R.id.startOnlineGme)
        var enterMyId = dialog.findViewById<EditText>(R.id.editTextText)
        var enterOpponentId = dialog.findViewById<EditText>(R.id.editTextText2)
        startOnlineGame.setOnClickListener {
            // todo check ids
            if(checkIds(enterMyId.text.toString(),enterOpponentId.text.toString()) == "ok") {
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                intent.putExtra("mode", true)
                intent.putExtra("myID", enterMyId.text.toString())
                intent.putExtra("opponentID", enterOpponentId.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this,"Something goes wrong",Toast.LENGTH_SHORT).show()
            }
        }
        setContentView(R.layout.activity_menu)
        val onlineButton : Button = findViewById(R.id.playOnline)
        val offlineButton : Button = findViewById(R.id.playOffline)
        val playWithAFriendButton : Button = findViewById(R.id.playWithAFriend)
        //offlineButton.playSoundEffect(SoundEffectConstants.CLICK)
        offlineButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)

            startActivity(intent)
        }
        onlineButton.setOnClickListener {
            val findGameServer = FindGameServer(this@MenuActivity)
            dialog.show() // this is loading strip
            // if you donnot see it, go to the doctor

        }
        playWithAFriendButton.setOnClickListener {
            dialog.show()
        }

    }
    fun startOnlineGame(myID: String, opponentID: String) {
        val intent = Intent(this@MenuActivity, MainActivity::class.java)
        intent.putExtra("mode", true)
        intent.putExtra("myID", myID)
        intent.putExtra("opponentID", opponentID)
        startActivity(intent)
    }

    private fun checkIds(myID: String?, opponentID: String?): String {
        if(myID == null || opponentID == null)
            return "null error"
        if(myID == "")
            return "enter your id"
        if(opponentID == "")
            return "enter your opponent id"
        if (myID == opponentID)
            return "ids should be different"
        // todo add check if opponent id exists
        val regex = Regex("[^a-zA-Z\\d_\\- а-яА-Я]")
        if(regex.containsMatchIn(myID) || regex.containsMatchIn(opponentID))
            return "[^a-zA-Z\\d_\\- а-яА-Я]"
        return "ok"
    }
}