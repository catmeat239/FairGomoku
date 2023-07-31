package com.antonshvarts.fairgomoku.activity
import android.app.Dialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.antonshvarts.fairgomoku.R
import com.antonshvarts.fairgomoku.online.FindGameServer

class MenuActivity : AppCompatActivity() {
     lateinit var dialog : Dialog
     lateinit var progressBar : ProgressBar
     var findGameServer : FindGameServer? = null
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
        val playWithTHeComputerButton : Button = findViewById(R.id.playWithTheComputer)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        // offlineButton.playSoundEffect(SoundEffectConstants.CLICK)
        offlineButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)
            intent.putExtra("withTheComputer", false)
            startActivity(intent)
        }
        onlineButton.setOnClickListener {
            findGameServer = FindGameServer(this@MenuActivity)
            progressBar.visibility = View.VISIBLE
        }
        playWithTHeComputerButton.setOnClickListener{
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)
            intent.putExtra("withTheComputer", true)
            startActivity(intent)
        }
        playWithAFriendButton.setOnClickListener {
            dialog.show()
        }

    }
    override fun onDestroy() {
        progressBar.visibility = View.INVISIBLE
        if(findGameServer != null) findGameServer!!.clear()
        super.onDestroy()
    }
    override fun onStop() {
        super.onStop()
        progressBar.visibility = View.INVISIBLE
        if(findGameServer != null) findGameServer!!.clear()
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

    fun startOnlineGame(myID: String, opponentID: String) {
        progressBar.visibility = View.GONE
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