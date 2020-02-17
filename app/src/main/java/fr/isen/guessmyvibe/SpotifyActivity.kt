package fr.isen.guessmyvibe

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.Result
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_spotify.*
import java.util.concurrent.TimeUnit


class SpotifyActivity : AppCompatActivity() {

    private val CLIENT_ID = "53e8b8b1bb7244f1ac977da374705352"
    private val REDIRECT_URI = "https://www.google.fr"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var CONNECTED = 0
    private var STATE = 0
    private var STEP = 1

    var counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify)


        if(STEP <10)
        {
            response1.setOnClickListener(){
                STEP = playNext(STEP)
            }
            response2.setOnClickListener(){
                STEP = playNext(STEP)
            }
            response3.setOnClickListener(){
                STEP = playNext(STEP)
            }
            response4.setOnClickListener(){
                STEP = playNext(STEP)
            }
        }

        searchButton.setOnClickListener(){
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
                    playSong()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun playSong() { // Then we will write some more code here.
        mSpotifyAppRemote?.getPlayerApi()?.play("spotify:playlist:7nWLr7ueGPIjP6Guk9TIc8")
        Toast.makeText(this, "played", Toast.LENGTH_LONG).show()



        val playerState = mSpotifyAppRemote?.playerApi?.playerState

        playerState?.setResultCallback {
            Toast.makeText(this,it.track.name, Toast.LENGTH_LONG).show()
        }

       // val playerStateResult = playerState?.await(5, TimeUnit.SECONDS)

       // val track: Track? = playerStateResult?.data?.track




        //startTimer()
    }

    fun startTimer() {

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = 10
                time = time - counter
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
                playNext(STEP)
                STATE =1

            }
        }.start()
    }
    fun playNext(s : Int) : Int{
        var step : Int
        step = s
       if(step < 10)
       {
           mSpotifyAppRemote?.getPlayerApi()?.skipNext()
           Toast.makeText(this, "played", Toast.LENGTH_LONG).show()


           val playerStateCall: CallResult<PlayerState>? = mSpotifyAppRemote?.playerApi?.playerState


           val playerStateResult: Result<PlayerState>? = playerStateCall?.await(10, TimeUnit.SECONDS)



           val track: Track? = playerStateResult?.data?.track

           Toast.makeText(this,track?.name, Toast.LENGTH_LONG).show()
           //startTimer()
           step++
           idSong.setText("Son " + step.toString() + "/10")

       }
        return step
    }




    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    fun pauseSong(){
        mSpotifyAppRemote?.getPlayerApi()?.pause()


    }

    fun getTrackName(){

    }



}
