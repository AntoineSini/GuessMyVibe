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
import fr.isen.guessmyvibe.classes.statusList

import kotlinx.android.synthetic.main.activity_multi.*
import kotlinx.android.synthetic.main.activity_register.*


class MultiActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage : FirebaseStorage
    var currentUser : User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()

        findCurrentUser()
        buttonsListener()
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
            }
        }
        users.addValueEventListener(userListener)
    }
    fun buttonsListener()
    {
        createButton.setOnClickListener{
            addGameToDatabase()
            intent= Intent(this, NewRoomActivity::class.java)
            startActivity(intent)

        }
        joinRoomButton.setOnClickListener{
            intent= Intent(this, JoinRoomActivity::class.java)
            startActivity(intent)
        }

    }
    fun addGameToDatabase(){
        val arraySingleUser = ArrayList<String>()//liste des id des joueurs de la partie
        currentUser?.let{//on ajoute le joueur courant
            arraySingleUser.add(it.id)
        }

        val key = database.child("game").push().key ?: ""
        val newGame = Game(key, arraySingleUser,null,statusList[0],null,"","Multiplayer")
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
