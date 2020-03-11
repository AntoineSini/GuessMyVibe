package fr.isen.guessmyvibe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.Score
import fr.isen.guessmyvibe.classes.User
import fr.isen.guessmyvibe.classes.statusList
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    var currentUser : User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        buttonsListener()
    }


    fun buttonsListener()
    {
        soloButton.setOnClickListener{
            addGameWithUser()
            intent= Intent(this, SoloEasyGameActivity::class.java)
            startActivity(intent)


        }
        multiplayersButton.setOnClickListener{
            intent= Intent(this, MultiActivity::class.java)
            startActivity(intent)


        }
        profileButton.setOnClickListener{
            intent= Intent(this, ProfileActivity::class.java)
            startActivity(intent)


        }
    }
    fun addGameWithUser() {
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
                addGameToDatabase()
            }
        }
        users.addListenerForSingleValueEvent(userListener)
    }

    fun addGameToDatabase(){
        val arraySingleUser = ArrayList<String>()//liste des id des joueurs de la partie
        currentUser?.id?.let {
            arraySingleUser.add(it)
        }
        val key = database.child("game").push().key ?: ""
        val newGame = Game(key, arraySingleUser, null,
            statusList[1],null,"Solo",currentUser?.id as String,"0")
        database.child("game").child(key).setValue(newGame)


        val id = currentUser?.id
        if(currentUser?.id_games == null) {
            val arraySingleGame = ArrayList<String>()
            arraySingleGame.add(newGame.id)
            id?.let {
                database.child("user").child(it).child("id_games").setValue(arraySingleGame)
            }
        }
        else {
            val arrayGames = currentUser?.id_games
            arrayGames?.add(newGame.id)
            id?.let {
                database.child("user").child(it).child("id_games").setValue(arrayGames)
            }
        }
    }
}
