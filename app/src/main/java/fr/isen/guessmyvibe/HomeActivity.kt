package fr.isen.guessmyvibe

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val url = "http://www.hochmuth.com/mp3/Haydn_Cello_Concerto_D-1.mp3"
        val mediaPlayer: MediaPlayer? = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        //mediaPlayer?.prepare()
        //mediaPlayer?.start()
        buttonsListener()
    }

    fun buttonsListener()
    {
        soloButton.setOnClickListener{
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)


        }
        multiplayersButton.setOnClickListener{
            intent= Intent(this, MultiActivity::class.java)
            startActivity(intent)


        }
        profileButton.setOnClickListener{
            intent= Intent(this, ProfileActivity::class.java)
            startActivity(intent)


        }
    }
}
