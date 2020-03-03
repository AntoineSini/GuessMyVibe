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
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View


class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    lateinit var storage: FirebaseStorage
    var imageUri : Uri? = null
    val GALLERY = 1
    val CAMERA = 2
    private val spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference

        logOutButton.setOnClickListener{
            auth.signOut()
            auth.addAuthStateListener {
                intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        progressBar.visibility = View.GONE
        userPpImageView.setOnClickListener{
            choosePhotoFromGallary()

        }
        //savePPToStorage()
        displayPP()

    }
    fun savePPToStorage(){
        val storageRef = storage.reference
        val uri = imageUri
        if (uri != null){
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

    fun displayPP(){
        storage.reference.child("${auth.currentUser?.uid}/profilePicture").downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(userPpImageView)
        }.addOnFailureListener {
            Toast.makeText(this, "Image failed to be displayed", Toast.LENGTH_SHORT)
        }
    }


    fun choosePhotoFromGallary(){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == GALLERY) {
                data?.data?.let {
                    imageUri = it
                }
                    savePPToStorage()
            }
            if(requestCode == CAMERA) {

            }
        }
    }
}
