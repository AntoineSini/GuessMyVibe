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
                    val p = postSnapshot.value as HashMap<String, Any>
                    if (p["id"] == auth.currentUser?.uid) {
                        val id = p["id"] as String
                        val email = p["email"] as String
                        val birthday = p["birthday"] as String?
                        val username = p["username"] as String?
                        val age = p["age"] as String?
                        val level = p["level"] as String
                        /*val games = p["games"] as ArrayList<HashMap<String, Any>>
                        val arrayGames = ArrayList<Game>()
                        for(item in games){
                            var diff : String?
                            var theme : String?
                            var status : String?
                            var winner : String?
                            for((k,v) in item){
                                diff = if(k=="difficulty") v as String else null
                                theme = if(k=="theme") v as String else null
                                status = if(k=="status") v as String else null
                                winner = if(k=="winner") v as String else null
                            }
                            val tmpGame = Game()
                        }*/
                        id?.let { id ->
                            email?.let { email ->
                                level?.let { level ->
                                    currentUser = User(id, email, birthday, username, age, level, null)
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
            createGameInDatabase()
            intent= Intent(this, NewRoomActivity::class.java)
            startActivity(intent)

        }
        joinRoomButton.setOnClickListener{
            intent= Intent(this, JoinRoomActivity::class.java)
            startActivity(intent)
        }

    }
    fun createGameInDatabase(){
        val arraySingleUser = ArrayList<User>()//liste des joueurs de la partie
        currentUser?.let{//on ajoute le joueur courant
            arraySingleUser.add(it)
        }
        val newGame = Game(arraySingleUser,null,"preparation",null,"","Multiplayer")
        val key = database.child("game").push().key ?: ""
        database.child("game").child(key).setValue(newGame)

        val id = currentUser?.id
        if(currentUser?.games == null) {
            val arraySingleGame = ArrayList<Game>()
            arraySingleGame.add(newGame)
            id?.let {
                database.child("user").child(it).child("games").setValue(arraySingleGame)
            }
        }
        else {
            val arrayGames = currentUser?.games
            arrayGames?.add(newGame)
            id?.let {
                database.child("user").child(it).child("games").setValue(arrayGames)
            }
        }
    }
}
