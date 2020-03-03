package fr.isen.guessmyvibe

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private var INSTALL = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val isAppInstalled: Boolean = appInstalledOrNot("com.spotify.music")
        if(isAppInstalled == false)
        {
            val dialogBuilder = AlertDialog.Builder(this)

            dialogBuilder.setMessage("L'utilisation de ce jeu requiert l'installation de Spotify !")

                .setNegativeButton("ok", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                    finishAffinity()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=com.spotify.music")
                    startActivity(intent)
                })

            val alert = dialogBuilder.create()
            alert.setTitle("Attention")
            alert.show()
        }


        buttonsListener()
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    fun buttonsListener()
    {
        soloButton.setOnClickListener{
            //intent= Intent(this, LevelActivity::class.java)
            intent= Intent(this, CategoryActivity::class.java)
            startActivity(intent)


        }
        multiplayersButton.setOnClickListener{
            intent= Intent(this, MultiActivity::class.java)
            startActivity(intent)


        }
        profileButton.setOnClickListener{
            intent= Intent(this, ProfileActivity::class.java)
            startActivity(intent)


        }
    }
}
