package fr.isen.guessmyvibe

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.CallResult
import com.spotify.protocol.client.Result
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_solo_easy.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit


class SoloEasyGameActivity : AppCompatActivity() {

    private val CLIENT_ID = "53e8b8b1bb7244f1ac977da374705352"
    private val REDIRECT_URI = "https://www.google.fr"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private var CONNECTED = 0
    private var STEP = 1
    private var CATEGORY : String ? = null

    var counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_easy)

        response1.setOnClickListener{
            timer()
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
            object : Connector.ConnectionListener {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.e("Connexion", "connexion ok")

                    // Now you can start interacting with App Remote
                    playPlaylist()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("Connexion failed", throwable.message, throwable)

                    showConnexionProblem()
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    fun showConnexionProblem(){
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("Vous n'etes pas connecté à Spotify")

            .setNegativeButton("ok", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                finishAffinity()
                val launchIntent = packageManager.getLaunchIntentForPackage("com.spotify.music")
                launchIntent?.let { startActivity(it) }
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Attention")
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showButtonsText(){

        var random = ThreadLocalRandom.current().nextInt(1, 4)

        if(random == 1)
        {
            response1.setText(getTrackName())
        }
        if(random == 2)
        {
            response2.setText(getTrackName())
        }
        if(random == 3)
        {
            response3.setText(getTrackName())
        }
        if(random == 4)
        {
            response4.setText(getTrackName())
        }





    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun playPlaylist() { // Then we will write some more code here.
        mSpotifyAppRemote?.playerApi?.play(getPlaylist())
        timer()
    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getPlaylist() : String
    {
        var playlist = "spotify:playlist:2IamgqJjhiz48fBY9W0kpa"
        var random = ThreadLocalRandom.current().nextInt(1, 5)


        if (CATEGORY == "Pop") //pop
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DWYVURwQHUqnN" // pop urbaine
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DX1ngEVM0lKrb" // pop internationale
            if (random == 3) playlist = "spotify:playlist:37i9dQZF1DX92MLsP3K1fI" // top pop
            if (random == 4) playlist = "spotify:playlist:37i9dQZF1DWYX0SFpLcPgx" // pop collection
            if (random == 5) playlist = "spotify:playlist:37i9dQZF1DWXti3N4Wp5xy" // pop party
        }

        if (CATEGORY == "Rock") //rock
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DX8FwnYE6PRvL" // rock party
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DWWSuZL7uNdVA" // top of the rock
            if (random == 3) playlist = "spotify:playlist:5BygwTQ3OrbiwVsQhXFHMz" // classic rock
            if (random == 4) playlist = "spotify:playlist:37i9dQZF1DWXTHBOfJ8aI7" // legendes du rock
            if (random == 5) playlist = "spotify:playlist:37i9dQZF1DXcF6B6QPhFDv" // rock this
        }

        if (CATEGORY == "Jazz") //jazz
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DXbITWG1ZJKYt" // jazz classics
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DX1S1NduGwpsa" // jazz club
            if (random == 3) playlist = "spotify:playlist:37i9dQZF1DX7YCknf2jT6s" // state of jazz
            if (random == 4) playlist = "spotify:playlist:37i9dQZF1DXbHcQpOiXk1D" // jazz uk
            if (random == 5) playlist = "spotify:playlist:37i9dQZF1DX2mmt7R81K2b" // jazz classical crossings
        }

        if (CATEGORY == "Blues") //blues
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DXd9rSDyQguIk" // blues classics
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DXcFk5r8uS3l2" // blues roots
            if (random == 3) playlist = "spotify:playlist:2ZPbHrBWpcnEsWWO00szW8" // blues guitar
            if (random == 4) playlist = "spotify:playlist:5TkTomPbQuSNDxdlWg2fCx" // blues masters
            if (random == 5) playlist = "spotify:playlist:26lao4thh1peAzKViwFBH8" // blues legends
        }


        if (CATEGORY == "Rap") //rap
        {
            if (random == 1) playlist = "spotify:playlist:4l1CEhc7ZPbaEtiPdCSGbl" // rap francais 2020
            if (random == 2) playlist = "spotify:playlist:4oVXvXoJgYHsbcRPkKEWLe" // rap us
            if (random == 3) playlist = "spotify:playlist:5qmvm8zOG9hbzrFdRarmIZ" // rap supreme
            if (random == 4) playlist = "spotify:playlist:37i9dQZF1DX0XUsuxWHRQd" // rap caviar
            if (random == 5) playlist = "spotify:playlist:44S09uTRLrW1xph1JD5lKJ" // rap punchline
        }

        if (CATEGORY == "70") //70's
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DX7LGssahBoms" // annees 70
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DX7W8X7B8YNLZ" // generation 70
            if (random == 3) playlist = "spotify:playlist:37i9dQZF1DWTJ7xPn4vNaz" // all out 70's
            if (random == 4) playlist = "spotify:playlist:5KmBulox9POMt9hOt3VV1x" // 70s smash hits
            if (random == 5) playlist = "spotify:playlist:1brybfKfiWT1sFitZOKpXO" // 70s disco party
        }

        if (CATEGORY == "80") //80's
        {
            if (random == 1) playlist = "spotify:playlist:1b5JNI6c8TliOecILKg8Vw" // annees 80 france
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DWWl7MndYYxge" // annees 80
            if (random == 3) playlist = "spotify:playlist:37i9dQZF1DX0zyaFj8e28t" // generation 80
            if (random == 4) playlist = "spotify:playlist:19PgP2QSGPcm6Ve8VhbtpG" // 80s smash hits
            if (random == 5) playlist = "spotify:playlist:37i9dQZF1DX4UtSsGT1Sbe" // all out 80s
        }

        if (CATEGORY == "90") //90's
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DWWGI3DKkKGzJ" // annees 90
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DWXLbJb1PtkXq" // generation 90
            if (random == 3) playlist = "spotify:playlist:37i9dQZF1DX6VDO8a6cQME " // i love 90s rnb
            if (random == 4) playlist = "spotify:playlist:3C64V048fGyQfCjmu9TIGA" // 90s smash hits
            if (random == 5) playlist = "spotify:playlist:37i9dQZF1DXbTxeAdrVG2l" // all out 90s
        }

        if (CATEGORY == "2000") //2000's
        {
            if (random == 1) playlist = "spotify:playlist:37i9dQZF1DXacPj7eARo6k" // annees 2000
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DWSvv6VnIb3i0" // generation 2000
            if (random == 3) playlist = "spotify:playlist:1Tm7ZRHpZozsynBgr9bh9I" // 2000s hits
            if (random == 4) playlist = "spotify:playlist:59rA0GEys4f4qvJCKmFfiA" // 2000 france
            if (random == 5) playlist = "spotify:playlist:7oaBsni6C0xBGjjuDyzYxJ" // playlist 2000
        }

        if (CATEGORY == "France") //France
        {
            if (random == 1) playlist = "spotify:playlist:6QyJmYAjsOdQXG39V4teg9" // france
            if (random == 2) playlist = "spotify:playlist:37i9dQZF1DXd0Y4aXXQXWv" // essentiel de la variete francaise
            if (random == 3) playlist = "spotify:playlist:6OKnBuHwgm4EJmxS8QbFC2" // variete francaise 2000-2019
            if (random == 4) playlist = "spotify:playlist:16e8q5FYKDsDXThuhN1S6m" // varietes francaises
            if (random == 5) playlist = "spotify:playlist:78ZTUJ2PKfs34LAMa8Qcsm" // veietes francaises 60 - 2000
        }

        return playlist
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun timer() {


        object : CountDownTimer(11000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                textView.setText((millisUntilFinished/1000).toString())
                progressBar.setProgress((millisUntilFinished/1000).toInt() * 10)
                //showButtonsText()
            }

            override fun onFinish() {
                if(STEP < 10)
                {
                    mSpotifyAppRemote?.playerApi?.skipNext()
                    getTrackName()
                    STEP++
                    idSong.setText("Son " + STEP.toString() + "/10")
                    timer()
                }
                else{
                    finishGame()
                }
            }
        }.start()
    }


    fun finishGame(){
        intent= Intent(this, EndSoloActivity::class.java)
        startActivity(intent)
    }



    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }



    fun getTrackName() : String? {

        val playerStateCall: CallResult<PlayerState>? = mSpotifyAppRemote?.playerApi?.playerState

        val playerStateResult: Result<PlayerState>? = playerStateCall?.await(10, TimeUnit.SECONDS)

        val track: Track? = playerStateResult?.data?.track

        return track?.name

    }



}
