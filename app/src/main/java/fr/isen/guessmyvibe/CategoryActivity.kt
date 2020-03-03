package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : AppCompatActivity() {

    private var CATEGORY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        buttonsListener()


    }

    fun buttonsListener(){

        popButton.setOnClickListener{
            CATEGORY = 1
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        rockButton.setOnClickListener{
            CATEGORY = 2
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        jazzButton.setOnClickListener{
            CATEGORY = 3
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        bluesButton.setOnClickListener{
            CATEGORY = 4
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        rapButton.setOnClickListener{
            CATEGORY = 5
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        seventiesButton.setOnClickListener{
            CATEGORY = 6
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        eightiesButton.setOnClickListener{
            CATEGORY = 7
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        ninetiesButton.setOnClickListener{
            CATEGORY = 8
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        twothousandsButton.setOnClickListener{
            CATEGORY = 9
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }
        frenchButton.setOnClickListener{
            CATEGORY = 10
            intent= Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }




    }
}
