package fr.isen.guessmyvibe.classes

import com.google.firebase.database.IgnoreExtraProperties

val statusList = listOf("Selection", "Current", "Finished")

@IgnoreExtraProperties
class Game (
    var id : String,
    var id_players : ArrayList<String>,
    var scores : ArrayList<Score>? = null,
    var status : String,
    var id_winner : String? = null,
    var theme : String,
    var difficulty : String
)
