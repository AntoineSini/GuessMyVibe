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

        var path = "fr.isen.guessmyvibe:drawable/" + code
        var id = resources.getIdentifier(code, "drawable", this.packageName)
        flagView.setImageResource(id)
        Toast.makeText(this, path + "," + id.toString(), Toast.LENGTH_LONG).show()



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

                }


            },
            Response.ErrorListener {
                Log.d("volley", it.toString())
            })
        queue.add(stringRequest)

    }


}
