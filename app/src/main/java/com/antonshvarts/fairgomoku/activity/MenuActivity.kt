package com.antonshvarts.fairgomoku.activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.antonshvarts.fairgomoku.R
import com.antonshvarts.fairgomoku.online.FindGameServer

class MenuActivity : AppCompatActivity() {
     private lateinit var playOnlineDialog: Dialog
     private lateinit var playWithAFriendDialog : Dialog
     private lateinit var botDifficaltyDialog: DialogFragment
     private var findGameServer : FindGameServer? = null

    private fun createPlayWithAFriendDialog() {

        playWithAFriendDialog = Dialog(this)
        playWithAFriendDialog.setTitle("Set game parametres")
        playWithAFriendDialog.setContentView(R.layout.settings)
        var startOnlineGame = playWithAFriendDialog.findViewById<Button>(R.id.startOnlineGme)
        var enterMyId = playWithAFriendDialog.findViewById<EditText>(R.id.editTextText)
        var enterOpponentId = playWithAFriendDialog.findViewById<EditText>(R.id.editTextText2)
        startOnlineGame.setOnClickListener {
            // todo check ids
            if(checkIds(enterMyId.text.toString(),enterOpponentId.text.toString()) == "ok") {
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                intent.putExtra("mode", true)
                intent.putExtra("myID", enterMyId.text.toString())
                intent.putExtra("opponentID", enterOpponentId.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this,"Something goes wrong",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createPlayOnlineDialog() {
        playOnlineDialog = Dialog(this)
        playOnlineDialog.setCancelable(false)
        playOnlineDialog.setContentView(R.layout.activity_play_online_acttivity)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        playOnlineDialog.setTitle("Waiting for opponent")
        var cancelOnlineGame = playOnlineDialog.findViewById<Button>(R.id.cancelOnlineGame)
        cancelOnlineGame.setOnClickListener {
            if(findGameServer != null) findGameServer!!.clear()
            playOnlineDialog.cancel()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        createPlayOnlineDialog()

        botDifficaltyDialog = DialogFragment()

        createPlayWithAFriendDialog()



        val onlineButton : Button = findViewById(R.id.playOnline)
        val offlineButton : Button = findViewById(R.id.playOffline)
        val playWithAFriendButton : Button = findViewById(R.id.playWithAFriend)
        val playWithTHeComputerButton : Button = findViewById(R.id.playWithTheComputer)

        // offlineButton.playSoundEffect(SoundEffectConstants.CLICK)
        offlineButton.setOnClickListener {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)
            intent.putExtra("withTheComputer", false)
            startActivity(intent)
        }
        onlineButton.setOnClickListener {
            findGameServer = FindGameServer(this@MenuActivity)
            playOnlineDialog.show()
            //progressBar.visibility = View.VISIBLE
        }
        playWithTHeComputerButton.setOnClickListener{
            val manager = supportFragmentManager
            botDifficaltyDialog.show(manager,"Choose difficalty")
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            intent.putExtra("mode",false)
            intent.putExtra("withTheComputer", true)
            startActivity(intent)
        }
        playWithAFriendButton.setOnClickListener {
            playWithAFriendDialog.show()
        }

    }
    override fun onDestroy() {
        if(findGameServer != null) findGameServer!!.clear()
        super.onDestroy()
    }
    override fun onStop() {
        super.onStop()
        if(findGameServer != null) findGameServer!!.clear()
    }
    override fun onBackPressed() {
        val backToMenuDialogBuilder =  AlertDialog.Builder(this)

        backToMenuDialogBuilder.setMessage("Are you sure?")
        backToMenuDialogBuilder.setPositiveButton("Yes") { _, _ -> {}

            super.onBackPressed()
        }
        backToMenuDialogBuilder.setNegativeButton("No") { _, _ -> {} }
        backToMenuDialogBuilder.setCancelable(true)
        backToMenuDialogBuilder.show()
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