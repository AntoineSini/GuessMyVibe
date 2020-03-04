package fr.isen.guessmyvibe.classes

import com.google.firebase.database.IgnoreExtraProperties

val statusList = listOf("Prepared", "Launched", "Finished")

@IgnoreExtraProperties
class Game (
    var players : ArrayList<User>,
    var scores : ArrayList<Score>? = null,
    var status : String,
    var winner : User? = null,
    var theme : String,
    var difficulty : String
)
