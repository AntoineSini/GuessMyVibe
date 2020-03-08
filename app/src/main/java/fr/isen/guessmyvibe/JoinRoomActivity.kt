package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.User
import kotlinx.android.synthetic.main.activity_join_room.*


class JoinRoomActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    var currentUser : User? = null
    var game : Game? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_room)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        findCurrentUser()

        joinButton.setOnClickListener {
            findGameById(joinRoomTextInput.text.toString())
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
                        val id = p["id"]
                        val email = p["email"]
                        val birthday = p["birthday"]
                        val username = p["username"]
                        val age = p["age"]
                        val level = p["level"]
                        val id_games = p["id_games"] as ArrayList<String>?
                        id?.let { id ->
                            email?.let { email ->
                                level?.let { level ->
                                    currentUser =
                                        User(id, email, birthday, username, age, level, id_games)
                                }
                            }
                        }
                    }
                }
            }
        }
        users.addValueEventListener(userListener)
    }
    fun findGameById(idGame: String){

        val games = database.child("game")
        games.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if(p["id"] == idGame) {
                        val id = p["id"] as String
                        val id_players = p["id_players"] as ArrayList<String>
                        //val scores = p["scores"] as ArrayList<>
                        val status = p["status"] as String
                        val id_winner = p["id_winner"]
                        val theme = p["theme"] as String
                        val difficulty = p["difficulty"] as String
                        game = Game(id, id_players, null, status, id_winner,theme, difficulty)
                    }
                }
                joinTheGame()
            }
        })
    }

    fun joinTheGame() {
        if (game != null) {
            currentUser?.let { user ->
                game?.let { game ->
                    game?.id_players?.add(user.id)
                    currentUser?.id_games?.add(game.id)
                    database.child("user").child(user.id).child("id_games").setValue(user.id_games)
                    database.child("game").child(game.id).child("id_players").setValue(game.id_players)
                    intent= Intent(this, NewRoomActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
