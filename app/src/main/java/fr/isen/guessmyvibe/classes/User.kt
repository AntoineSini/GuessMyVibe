package fr.isen.guessmyvibe.classes

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class User (
    var id : String,
    var email : String,
    var username : String?,
    var level : String,
    var id_games: ArrayList<String>?

)