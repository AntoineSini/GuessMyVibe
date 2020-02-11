package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_multi.*


class MultiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)
        buttonsListener()
    }

    fun buttonsListener()
    {
        createButton.setOnClickListener{
            intent= Intent(this, NewRoomActivity::class.java)
            startActivity(intent)

        }
        joinRoomButton.setOnClickListener{
            intent= Intent(this, JoinRoomActivity::class.java)
            startActivity(intent)
        }

    }
}
