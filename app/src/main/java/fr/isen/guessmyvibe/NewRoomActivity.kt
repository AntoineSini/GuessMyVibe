package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.User

import kotlinx.android.synthetic.main.activity_new_room.*


class NewRoomActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    lateinit var userArray : ArrayList<User>
    var currentUser : User? = null
    var currentGame : Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_room)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()


        playButton.setOnClickListener{
            intent= Intent(this, MultiplayersGameActivity::class.java)
            startActivity(intent)
        }
        findCurrentUser()
        //findCurrentGame()
        //findUserArray()
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
                                    currentUser = User(id, email, birthday, username, age, level, id_games)
                                }
                            }
                        }
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
                        val theme = p["theme"] as String
                        val difficulty = p["difficulty"] as String
                        currentGame= Game(id, id_players, null, status, id_winner,theme, difficulty)
                    }
                }
                findUserArray()
            }
        }
        games.addListenerForSingleValueEvent(gameListener)
    }
    fun findUserArray(){
        val users = database.child("user")
        val userListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var userIds = ArrayList<String>()
                currentGame?.id_players?.let{
                    userIds = it
                }
                var arrayUser = ArrayList<User>()
                for (iduser in userIds) {
                    for (postSnapshot in p0.children) {
                        val p = postSnapshot.value as HashMap<String, String>
                        if (p["id"] == iduser) {
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
                                        arrayUser.add(User(id, email, birthday, username, age, level, id_games))
                                    }
                                }
                            }
                        }
                    }
                }
                userArray = arrayUser
                recyclerHandler()
            }
        }
        users.addValueEventListener(userListener)
    }
    fun recyclerHandler() {
        playersRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = RecyclerAdapterRoom(content = userArray)
        playersRecyclerView.adapter = adapter
    }
}
