package it.uninsubria.takepizza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_recensione.*

class Account : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

    }
    fun onClick(v: View) {

        when (v.id) {
            R.id.buttonStar -> try {
                val intent = Intent(this, Recensione::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            R.id.buttonMaps -> try {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}