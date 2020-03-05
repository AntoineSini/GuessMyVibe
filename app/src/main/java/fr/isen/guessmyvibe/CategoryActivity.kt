package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : AppCompatActivity() {

    private var CATEGORY : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        buttonsListener()


    }

    fun buttonsListener(){

        popButton.setOnClickListener{
            CATEGORY = "Pop"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        rockButton.setOnClickListener{
            CATEGORY = "Rock"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        jazzButton.setOnClickListener{
            CATEGORY = "Jazz"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        bluesButton.setOnClickListener{
            CATEGORY = "Blues"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        rapButton.setOnClickListener{
            CATEGORY = "Rap"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        seventiesButton.setOnClickListener{
            CATEGORY = "70"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        eightiesButton.setOnClickListener{
            CATEGORY = "80"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        ninetiesButton.setOnClickListener{
            CATEGORY = "90"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        twothousandsButton.setOnClickListener{
            CATEGORY = "2000"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        frenchButton.setOnClickListener{
            CATEGORY = "French"
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }




    }
}
