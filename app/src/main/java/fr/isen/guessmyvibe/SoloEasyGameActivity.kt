package fr.isen.guessmyvibe

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.guessmyvibe.classes.FlagModel
import fr.isen.guessmyvibe.classes.Flags
import kotlinx.android.synthetic.main.activity_solo_easy.*
import java.util.concurrent.ThreadLocalRandom


class SoloEasyGameActivity : AppCompatActivity() {


    var flags: Flags? = null
    var size : Int = 0


    //@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_easy)
        requestRandomFlag()

        //var random = getRandomIndex()


    }


    /*@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getRandomIndex() : Int{

        return ThreadLocalRandom.current().nextInt(0, size -1 )
    }

    fun showFlags(index : Int){
        lateinit var drapeau : FlagModel

        flags.results?.get(index)?.let{
            drapeau = it
        }

        var path = "R.drawable." + drapeau.code
        //flagView.setImageResource(path.toInt())


    }*/

    fun requestRandomFlag(){

        val queue = Volley.newRequestQueue(this)
        val url = "https://puu.sh/Fj69u/d36bc26914.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                Log.d("volley", response)

                flags = Gson().fromJson(response, Flags::class.java)
                flags?.results?.size?.let{
                    size=it
                }
                var random=

            },
            Response.ErrorListener {
                Log.d("volley", it.toString())
            })
        queue.add(stringRequest)

    }


}
