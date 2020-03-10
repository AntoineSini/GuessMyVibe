package fr.isen.guessmyvibe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private var INSTALL = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        buttonsListener()
    }


    fun buttonsListener()
    {
        soloButton.setOnClickListener{
            intent= Intent(this, SoloEasyGameActivity::class.java)
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
