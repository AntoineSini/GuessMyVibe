package fr.isen.guessmyvibe

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import fr.isen.guessmyvibe.classes.Flags
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.User
import fr.isen.guessmyvibe.classes.statusList
import kotlinx.android.synthetic.main.activity_multiplayers_game.*
import kotlin.random.Random

class MultiplayersGameActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    var currentUser : User? = null
    var currentGame : Game? = null
    var flags: Flags? = null
    var size : Int = 0
    var response: Int = 0
    var points: Int = 0
    var pts : Int = 100
    var STEP =0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayers_game)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        findCurrentUser()

        requestRandomFlag()


        /*finished.setOnClickListener{
            var finished : Int = currentGame?.finished?.toInt() as Int
            finished++
            val finishedString = finished.toString()
            currentGame?.id?.let{
                database.child("game").child(it).child("finished").setValue(finishedString)
            }
            findCurrentUser()
        }*/
    }

    /// ANTOINE ////
    fun finishTheGame(){
        val size = currentGame?.id_players?.size?.toString()
        if(currentGame != null) {
            if (currentGame?.finished == size) {
                currentGame?.status = statusList[2]
                currentGame?.id?.let {
                    database.child("game").child(it).child("status").setValue(currentGame?.status)
                }
                intent = Intent(this, EndSoloActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun findCurrentUser() {
        val users = database.child("user")
        val userListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if (p["id"] == auth.currentUser?.uid) {
                        val id = p["id"] as String
                        val email = p["email"] as String
                        val username = p["username"]
                        val level = p["level"] as String
                        val id_games = p["id_games"] as ArrayList<String>?
                        currentUser = User(id, email, username, level, id_games)
                    }
                }
                findCurrentGame()
            }
        }
        users.addListenerForSingleValueEvent(userListener)
    }
    fun findCurrentGame() {
        val games = database.child("game")
        val gameListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var lastGame = currentUser?.id_games?.last()

                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if (p["id"] == lastGame) {
                        val id = p["id"] as String
                        val id_players = p["id_players"] as ArrayList<String>
                        //val scores = p["scores"] as ArrayList<>
                        val status = p["status"] as String
                        val id_winner = p["id_winner"]
                        val theme = p["theme"] as String
                        val difficulty = p["difficulty"] as String
                        val id_owner = p["id_owner"] as String
                        val finished = p["finished"] as String
                        currentGame = Game(id,id_players,null,status,id_winner,theme,difficulty,id_owner, finished)
                    }
                }
                finishTheGame()
            }
        }
        games.addListenerForSingleValueEvent(gameListener)
    }

    /// MORGANE ///
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getRandomIndex() : Int{

        return Random.nextInt(size)
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
            startGame()
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

        var random = Random.nextInt(4)
        setTextButton(random, country)
        tabButtons[random]=2
        cpt++
        response = random

        while(cpt<4) {
            var random = Random.nextInt(4)
            var randomCountry = Random.nextInt(size)

            while (tabButtons[random] == 0) {
                setTextButton(random, flags?.results?.get(randomCountry)?.country)
                tabButtons[random] = 1
                var random = Random.nextInt(4)
                cpt++
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startGame(){
        STEP++

        if(STEP <= 10)
        {

            if(STEP == 1){
                chronoView.start()
                val systemCurrTime = SystemClock.elapsedRealtime()
                chronoView.setBase(systemCurrTime)
            }

            chronoView.setOnChronometerTickListener(object : Chronometer.OnChronometerTickListener {
                var counter = 9
                override fun onChronometerTick(chronometer: Chronometer) {

                    chronoView.setText(counter.toString() + "")
                    progressBar.setProgress(counter * 10)

                    counter--

                    if (counter < 0) {
                        startGame()
                    }
                }
            })

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
        var finished : Int = currentGame?.finished?.toInt() as Int
        finished++
        val finishedString = finished.toString()
        currentGame?.id?.let{
            database.child("game").child(it).child("finished").setValue(finishedString)
        }
        findCurrentUser()
        /*intent= Intent(this, EndSoloActivity::class.java)
        startActivity(intent)*/
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
