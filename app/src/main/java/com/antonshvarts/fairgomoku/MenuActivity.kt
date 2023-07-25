package com.antonshvarts.fairgomoku
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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
            if(checkIds(enterMyId.text.toString(),enterOpponentId.text.toString())) {
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
        //offlineButton.playSoundEffect(SoundEffectConstants.CLICK)
        offlineButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)

            startActivity(intent)
        }
        onlineButton.setOnClickListener {
            dialog.show()
        }
    }

    private fun checkIds(myID: String?, opponentID: String?): Boolean {
        if(myID == null || opponentID == null)
            return false
        if(myID == "" || opponentID == "")
            return false
        if (myID == opponentID)
            return false
        // todo add check if opponent id exists
        return true
    }
}