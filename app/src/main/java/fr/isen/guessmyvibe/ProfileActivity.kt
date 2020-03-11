package fr.isen.guessmyvibe

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.guessmyvibe.classes.User
import kotlinx.android.synthetic.main.activity_profile.*
import android.widget.ProgressBar
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.guessmyvibe.classes.Game
import fr.isen.guessmyvibe.classes.Score
import android.R.string.cancel
import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage: FirebaseStorage
    lateinit var arrayGamesOfUser: ArrayList<Game>
    var currentUser: User? = null
    var imageUri: Uri? = null
    val GALLERY = 1
    val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference

        logOutButton.setOnClickListener {
            auth.signOut()
            auth.addAuthStateListener {
                intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        progressBar.visibility = View.GONE
        userPpImageView.setOnClickListener {
            choosePhotoFromGallary()
        }
        usernameTextView.setOnClickListener{
            alertDialog("Nom d'utilisateur")
        }

        displayPP()

        findCurrentUser()
        findGamesFromUser()

    }
    fun alertDialog(title : String){
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        with(builder){
            setTitle(title)
            input.inputType = InputType.TYPE_CLASS_TEXT
            setView(input)
            setPositiveButton("OK"){ _, _ ->
                currentUser?.id?.let {
                    database.child("user").child(it).child("username").setValue(input.text.toString())
                }
                findCurrentUser()
            }
            setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
            }

            show()
        }
    }
    fun textAdapt(){
        if (currentUser?.username != null) {
            val string = currentUser?.username
            usernameTextView.text = string
            level.text = currentUser?.level
        }
    }
    fun recyclerHandler() {
        lastGamesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = RecyclerAdapterProfile(content = arrayGamesOfUser)
        lastGamesRecyclerView.adapter = adapter
    }

    fun savePPToStorage() {
        val storageRef = storage.reference
        val uri = imageUri
        if (uri != null) {
            val pathRef = storageRef.child("${auth.currentUser?.uid}/profilePicture")
            val uploadTask = pathRef.putFile(uri)

            uploadTask.addOnFailureListener {
                Toast.makeText(this, "Upload failed ! :(", LENGTH_SHORT)
            }.addOnProgressListener {
                progressBar.visibility = View.VISIBLE
                userPpImageView.alpha = 0f
            }.addOnSuccessListener {
                Toast.makeText(this, "Upload successful ! :)", LENGTH_SHORT)
                progressBar.visibility = View.GONE
                displayPP()
                userPpImageView.alpha = 1f
            }
        }
    }

    fun displayPP() {
        storage.reference.child("${auth.currentUser?.uid}/profilePicture")
            .downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(userPpImageView)
        }.addOnFailureListener {
            Toast.makeText(this, "Image failed to be displayed", Toast.LENGTH_SHORT)
        }
    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
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
                textAdapt()
            }
        }
        users.addValueEventListener(userListener)
    }

    fun findGamesFromUser() {
        val games = database.child("game")
        val gameListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                Log.d("bug listener", "loadUser:onCancelled", p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val arrayGames = ArrayList<Game>()
                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    currentUser?.id_games?.let{
                        for(id in it){
                            if(p["id"] == id){
                                val id = p["id"] as String
                                val id_players = p["id_players"] as ArrayList<String>
                                //val scores = p["scores"] as ArrayList<>
                                val status = p["status"] as String
                                val id_winner = p["id_winner"]
                                val difficulty = p["difficulty"] as String
                                val id_owner = p["id_owner"] as String
                                val finished = p["finished"] as String
                                arrayGames.add(Game(id, id_players, null, status, id_winner, difficulty, id_owner, finished))
                            }
                        }
                    }
                }
                arrayGamesOfUser = arrayGames
                recyclerHandler()
            }
        }

        games.addValueEventListener(gameListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                data?.data?.let {
                    imageUri = it
                }
                savePPToStorage()
            }
            if (requestCode == CAMERA) {

            }
        }
    }
}
