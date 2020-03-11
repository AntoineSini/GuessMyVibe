package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.User
import fr.isen.guessmyvibe.classes.statusList

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
            if(currentGame?.status == statusList[0]){
                if(currentUser?.id == currentGame?.id_owner) {
                    currentGame?.status = statusList[1]
                    currentGame?.id?.let {
                        database.child("game").child(it).child("status").setValue(currentGame?.status)
                    }
                    intent = Intent(this, MultiplayersGameActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "The owner did not launch the game for the moment!",Toast.LENGTH_LONG).show()
                }
            }
            else if (currentGame?.status == statusList[1]){
                intent = Intent(this, MultiplayersGameActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "This game is finished, absolutely nonsense...",Toast.LENGTH_LONG).show()
            }

        }
        findCurrentUser()
    }
    override fun onDestroy() {
        super.onDestroy()
        var sizeMinus = 0
        currentUser?.id_games?.size?.let{
            sizeMinus = it
            sizeMinus--
        }
        currentGame?.id?.let {
            if (currentUser?.id == currentGame?.id_owner) {
                database.child("game").child(it).removeValue()
                currentUser?.id?.let {userId ->
                    database.child ("user").child(userId).child("id_games").child(sizeMinus.toString()).removeValue()
                }
            } else {
                //database.child("game").child(it).child("id_users").child(sizeMinus.toString()).removeValue()
                currentUser?.id?.let{userId ->
                    database.child("user").child(userId).child("id_games").child(sizeMinus.toString()).removeValue()
                }
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
                        val id_owner = p["id_owner"] as String
                        val finished = p["finished"] as String
                        currentGame= Game(id, id_players, null, status, id_winner,theme, difficulty, id_owner, finished)
                    }
                }
                findUserArray()
            }
        }
        games.addValueEventListener(gameListener)
    }

    fun findUserArray(){
        val users = database.child("user")
        val userListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                var arrayUser = ArrayList<User>()
                currentGame?.id_players?.let {
                    for (iduser in it) {
                        for (postSnapshot in p0.children) {
                            val p = postSnapshot.value as HashMap<String, String>
                            if (p["id"] == iduser) {
                                val id = p["id"] as String
                                val email = p["email"] as String
                                val username = p["username"]
                                val level = p["level"] as String
                                val id_games = p["id_games"] as ArrayList<String>?
                                arrayUser.add(User(id, email, username, level, id_games))
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
