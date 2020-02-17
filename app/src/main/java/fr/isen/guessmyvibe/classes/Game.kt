package fr.isen.guessmyvibe.classes

val statusList = listOf("Prepared", "Launched", "Finished")

class Game {
    var players : ArrayList<User>? = null
    var status : String = statusList[0]
    var winner : User? = null
}