package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.Score
import fr.isen.guessmyvibe.classes.User

import kotlinx.android.synthetic.main.activity_end_solo.*
import java.util.Collections.max


class EndSoloActivity : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    var currentUser : User? = null
    var winnername : String? = null
    var currentGame : Game? = null
    private var SCORE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_solo)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        findCurrentUser()
        findCurrentGame()

        replayButton.setOnClickListener{
            intent= Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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

    fun findCurrentGame(){
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
                        val difficulty = p["difficulty"] as String
                        val id_owner = p["id_owner"] as String
                        val finished = p["finished"] as String
                        currentGame= Game(id, id_players, null, status, id_winner, difficulty, id_owner, finished)
                    }
                }
                showPoints()
            }
        }
        games.addListenerForSingleValueEvent(gameListener)
    }
    fun findUserById(idUser : String) {

        val users = database.child("user")
        users.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if(p["id"] == idUser) {
                        winnername = p["username"] as String
                        if(winnername == null){
                            winnername = p["email"]
                        }
                    }
                }
                currentGame?.id?.let{
                    database.child("game").child(it).child("id_winner").setValue(winnername)
                }
            }
        })
    }

    fun showPoints(){

        val games = database.child("game")
        val gameListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {

                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if (p["id"] == currentGame?.id) {
                        val status = p["status"] as String
                        val scores = p["scores"] as HashMap<String, String>
                        if(status == "Finished")
                        {
                            var max = 0
                            lateinit var winner : String
                            for (i in scores)
                            {
                                val currentScore = i.value as HashMap<String, String>
                                val playerScore = currentScore["score"] as String
                                if (playerScore.toInt() > max)
                                {
                                    max = playerScore.toInt()
                                    winner = i.key
                                    findUserById(winner)
                                }

                            }


                        }
                        val currentScore = scores[currentUser?.id] as HashMap<String, String>
                        val playerScore = currentScore["score"] as String
                        pointsTextView.setText("Vous avez marqué " + playerScore + " points!")
                        var newLevel : Float = 0f
                        currentUser?.level?.let{
                            newLevel = it.toFloat() + (playerScore.toFloat()/1000)
                        }
                        currentUser?.id?.let {
                            database.child("user").child(it).child("level").setValue(newLevel.toString())
                        }

                    }
                }
            }
        }
        games.addListenerForSingleValueEvent(gameListener)


    }
}
