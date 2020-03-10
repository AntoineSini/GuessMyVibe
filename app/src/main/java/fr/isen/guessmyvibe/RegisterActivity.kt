package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.guessmyvibe.classes.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        registerButton.setOnClickListener{
            if(passwordTextInput.text.toString() == confirmTextInput.text.toString()){
                createUser(emailTextInput.text.toString(),passwordTextInput.text.toString())
            }
            else {
                Toast.makeText(this, "Les mots de passe sont différents!" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(email: String, password: String){
        if (email.isBlank()){
            Toast.makeText(this, "Adresse mail vide", Toast.LENGTH_SHORT).show()
        }
        else if (password.isBlank()){
            Toast.makeText(this, "Mot de passe vide", Toast.LENGTH_SHORT).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user'sq information
                    Log.d("antoine", "createUserWithEmail:success")
                    addUserToDatabase()
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("antoine", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    Toast.makeText(
                        this,
                        "Mot de passe de 6 caractères minimum",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun addUserToDatabase(){
        val id = auth.currentUser?.uid
        id?.let{
            val newUser = User(it, emailTextInput.text.toString(), null, "0", null)
            //val key = database.child("user").push().key ?: ""
            database.child("user").child(it).setValue(newUser)
        }
    }
}
