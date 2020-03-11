package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_end_solo.*


class EndSoloActivity : AppCompatActivity() {

    private var POINTS = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_solo)

        showPoints()

        replayButton.setOnClickListener{
            intent= Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun showPoints(){
        pointsTextView.setText(POINTS.toString() + " POINTS !")
    }
}
