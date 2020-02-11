package fr.isen.guessmyvibe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
            if(passwordTextInput.text.toString() == confirmTextInput.text.toString()){
                createUser(emailTextInput.text.toString(),passwordTextInput.text.toString())
            }
            else {
                Toast.makeText(baseContext, "Les mots de passe sont différents!" , Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun createUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user'sq information
                    Log.d("antoine", "createUserWithEmail:success")
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("antoine", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Registration failed" , Toast.LENGTH_SHORT).show()
                    Toast.makeText(baseContext, "Mot de passe de 6 caractères minimum" , Toast.LENGTH_SHORT).show()
                }
            }

    }
}
