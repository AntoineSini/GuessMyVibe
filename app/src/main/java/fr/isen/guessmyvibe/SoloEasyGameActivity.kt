package fr.isen.guessmyvibe

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.guessmyvibe.classes.Flags
import kotlinx.android.synthetic.main.activity_solo_easy.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random.Default.nextInt


class SoloEasyGameActivity : AppCompatActivity() {


    var flags: Flags? = null
    var size : Int = 0
    var response: Int = 0
    var points: Int = 0
    var pts : Int = 100
    var STEP =0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_easy)
        requestRandomFlag()
        //var random = getRandomIndex()


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getRandomIndex() : Int{

        return nextInt(size)
    }

    fun showFlags(code : String?){

        var id = resources.getIdentifier(code?.toLowerCase(), "drawable", "fr.isen.guessmyvibe")
        flagView.setImageResource(id)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun buttonsListener(){
        response1.setOnClickListener{
            if(response == 0){
                points += pts
            }
            startGame()
        }
        response2.setOnClickListener{
            if(response == 1){
                points += pts
            }
            startGame()

        }
        response3.setOnClickListener{
            if(response == 2){
                points += pts
            }
            startGame()

        }
        response4.setOnClickListener{
            if(response == 3){
                points += pts
            }
            else startGame()
        }


    }
    fun setTextButton(button : Int, text : String?)
    {

        if(button == 0)
        {
            response1.setText(text)
        }
        if(button == 1)
        {
            response2.setText(text)
        }
        if(button == 2)
        {
            response3.setText(text)
        }
        if(button == 3)
        {
            response4.setText(text)
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showResponses(country: String?, flags : Flags?){
        var tabButtons = arrayOf<Int>(0,0,0,0)
        var cpt=0

        var random = nextInt(4)
        setTextButton(random, country)
        tabButtons[random]=2
        cpt++
        response = random

        while(cpt<4) {
            var random = nextInt(4)
            var randomCountry = nextInt(size)

            while (tabButtons[random] == 0) {
                setTextButton(random, flags?.results?.get(randomCountry)?.country)
                tabButtons[random] = 1
                var random = nextInt(4)
                cpt++
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startGame(){
        STEP++
        if(STEP <= 10)
        {
            var random = getRandomIndex()
            showFlags(flags?.results?.get(random)?.code)
            showResponses(flags?.results?.get(random)?.country, flags)
            buttonsListener()
        }
        else{
            finishGame()
        }
    }

    fun finishGame(){
        intent= Intent(this, EndSoloActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun requestRandomFlag(){

        val queue = Volley.newRequestQueue(this)
        val url = "https://puu.sh/Fj7ZY/4bb0455f21.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                Log.d("volley", response)

                flags = Gson().fromJson(response, Flags::class.java)
                flags?.results?.size?.let{
                    size=it
                    startGame()
                }


            },
            Response.ErrorListener {
                Log.d("volley", it.toString())
            })
        queue.add(stringRequest)

    }


}
