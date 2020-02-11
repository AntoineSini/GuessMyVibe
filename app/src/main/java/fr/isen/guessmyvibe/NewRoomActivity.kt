package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_new_room.*


class NewRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_room)

        playButton.setOnClickListener{
            intent= Intent(this, MultiplayersGameActivity::class.java)
            startActivity(intent)
        }
    }
}
