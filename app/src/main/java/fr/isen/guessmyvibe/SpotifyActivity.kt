package fr.isen.guessmyvibe

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_spotify.*


class SpotifyActivity : AppCompatActivity() {

    private val CLIENT_ID = "53e8b8b1bb7244f1ac977da374705352"
    private val REDIRECT_URI = "https://www.google.fr"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var CONNECTED = 0

    var counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)


        playButton.setOnClickListener(){
            if (CONNECTED == 1){
                playSong()
            }

        }
        
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()


        SpotifyAppRemote.connect(this, connectionParams,
            object : ConnectionListener{
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    CONNECTED = 1
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun playSong() { // Then we will write some more code here.
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:track:24Yi9hE78yPEbZ4kxyoXAI")
        Toast.makeText(this, "played", Toast.LENGTH_LONG).show()

       startTimer()

    }

    fun startTimer() {

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time : Int
                time = 10 - counter
                if(time > 0){
                    textView.setText(time.toString())
                }
                counter++
                if (time == 0)
                {
                    onFinish()
                }
            }

            override fun onFinish() {
                textView.setText("FINISH!!")
                pauseSong()
            }
        }.start()

    }




    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    fun pauseSong(){
        mSpotifyAppRemote?.getPlayerApi()?.pause()
        Toast.makeText(this, "paused", Toast.LENGTH_LONG).show()


    }
}
