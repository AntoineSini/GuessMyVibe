package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_level.*


class LevelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        buttonsListener()
    }

    fun buttonsListener()
    {
        easyButton.setOnClickListener{
            intent= Intent(this, SoloEasyGameActivity::class.java)
            startActivity(intent)


        }
        hardButton.setOnClickListener{
            intent= Intent(this, SoloHardGameActivity::class.java)
            startActivity(intent)


        }

    }
}
