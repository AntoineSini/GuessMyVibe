package fr.isen.guessmyvibe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
