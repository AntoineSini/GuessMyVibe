package fr.isen.guessmyvibe.classes

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Score(
    var id_player : String,
    var id_game: String,
    var score: Int?
)
