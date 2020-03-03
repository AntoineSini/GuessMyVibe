package fr.isen.guessmyvibe.classes

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class User (
    var id : String?,
    var email : String,
    var birthday : String?,
    var username : String?,
    var age : Int?,
    var level : Int
)