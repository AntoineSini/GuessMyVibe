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



class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage: FirebaseStorage
    lateinit var currentUser: User
    var imageUri: Uri? = null
    val GALLERY = 1
    val CAMERA = 2
    private val spinner: ProgressBar? = null

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
        displayPP()
        //findCurrentUser()
    }

    fun recyclerAndDatabaseHandler(games: ArrayList<Game>) {
        lastGamesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = RecyclerAdapterProfile(content = currentUser.games as ArrayList<Game>)
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

    fun findCurrentUser() {
        val users = database.child("user")
        users.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val games = ArrayList<Game>()

                for (postSnapshot in p0.children) {
                    val p = postSnapshot.value as HashMap<String, String>
                    if (p["id"] == auth.currentUser?.uid) {
                        val id = p["id"]
                        val email = p["email"]
                        val birthday = p["birthday"]
                        val username = p["id"]
                        val age = p["age"]
                        val level = p["level"]
                        val games = p["games"]
                        id?.let { id ->
                            email?.let { email ->
                                level?.let{level ->
                                    currentUser = User(id, email, birthday, username, age, level, games as ArrayList<Game>)
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
