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
import fr.isen.guessmyvibe.classes.User
import fr.isen.guessmyvibe.classes.statusList
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
                        val id = p["id"] as String
                        val email = p["email"] as String
                        val username = p["username"]
                        val level = p["level"] as String
                        val id_games = p["id_games"] as ArrayList<String>?
                        currentUser = User(id, email, username, level, id_games)
                    }
                }
            }
        }
        users.addListenerForSingleValueEvent(userListener)
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
                        val id_owner = p["id_owner"] as String
                        val finished = p["finished"] as String
                        game = Game(id, id_players, null, status, id_winner,theme, difficulty, id_owner, finished)
                    }
                }
                joinGameWithUser()
            }
        })
    }
    fun joinGameWithUser() {
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
                joinTheGame()
            }
        }
        users.addListenerForSingleValueEvent(userListener)
    }
    fun joinTheGame() {
        var join = true

        game?.id_players?.let{
            for (id in it){
                if (id == currentUser?.id){
                    Toast.makeText(this,"You are still in this game, that's nonsense ...",Toast.LENGTH_LONG).show()
                    join = false
                }
            }
        }
        if(join) {
            val status = game?.status
            if (game != null && status == statusList[0]) {
                currentUser?.let { user ->
                    game?.let { game ->
                        if (user.id_games == null){
                            val arrayGames = ArrayList<String>()
                            arrayGames.add(game.id)
                            user.id_games = arrayGames

                        }
                        else{
                            user.id_games?.add(game.id)
                        }
                        game.id_players.add(user.id)
                        database.child("user").child(user.id).child("id_games").setValue(user.id_games)
                        database.child("game").child(game.id).child("id_players").setValue(game.id_players)
                        intent = Intent(this, NewRoomActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            else if (status == statusList[2]){
                Toast.makeText(this,"This gameis finished !",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "This game does not exist :(",Toast.LENGTH_LONG).show()
            }
        }
    }
}
