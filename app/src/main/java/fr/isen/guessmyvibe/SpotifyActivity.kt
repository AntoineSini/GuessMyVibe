package fr.isen.guessmyvibe

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)

        playButton.setOnClickListener(){
            if (CONNECTED == 1){
                connected()
            }
        }
        stopButton.setOnClickListener(){
                Toast.makeText(this, "stop btn", Toast.LENGTH_LONG).show()
                onStop()

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

    private fun connected() { // Then we will write some more code here.
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:playlist:37i9dQZF1DX4WpYT2fah9c")
    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
        mSpotifyAppRemote?.getPlayerApi()?.pause()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)


    }
}
