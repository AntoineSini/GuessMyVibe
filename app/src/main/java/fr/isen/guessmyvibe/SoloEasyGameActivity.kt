package fr.isen.guessmyvibe

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


class SoloEasyGameActivity : AppCompatActivity() {


    var flags: Flags? = null
    var size : Int = 0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_easy)
        requestRandomFlag()
        //var random = getRandomIndex()


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getRandomIndex() : Int{

        return ThreadLocalRandom.current().nextInt(0, size -1 )
    }

    fun showFlags(code : String?){

        var id = resources.getIdentifier(code?.toLowerCase(), "drawable", "fr.isen.guessmyvibe")
        flagView.setImageResource(id)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showResponses(country: String?, flags : Flags?){
        response1.setText(country)
        var random = getRandomIndex()
        response3.setText(flags?.results?.get(random)?.country)
        random = getRandomIndex()
        response4.setText(flags?.results?.get(random)?.country)
        random = getRandomIndex()
        response2.setText(flags?.results?.get(random)?.country)
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
                    var random = getRandomIndex()
                    showFlags(flags?.results?.get(random)?.code)
                    showResponses(flags?.results?.get(random)?.country, flags)

                }


            },
            Response.ErrorListener {
                Log.d("volley", it.toString())
            })
        queue.add(stringRequest)

    }


}
