package it.uninsubria.takepizza

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*


class Account : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "MainActivity"

    var databaseUsers: DatabaseReference? = null
    var database: FirebaseDatabase? = FirebaseDatabase.getInstance()
    var longitudine:String=""
    var latitudine:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()
        databaseUsers = database!!.getReference("Users");

        setUsrEmailpre()

    }


    fun setUsrEmailpre()
    {
        //set email
        val email: String? = auth.currentUser?.email
        accountEmailBox.text = email

        //set username
        var id: String = auth.getCurrentUser()!!.uid
        val username: DatabaseReference = databaseUsers!!.child(id).child("username")

        username.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.value.toString()
                usernameBox.text= username
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val preferita: DatabaseReference = databaseUsers!!.child(id).child("Pizzeria preferita")
        preferita.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!dataSnapshot.value.toString().contains("null"))
                {
                    val preferita = dataSnapshot.value.toString()
                    val refLatitudine: DatabaseReference = databaseUsers!!.child(id).child("Latitudine")
                    val refLongitudine: DatabaseReference = databaseUsers!!.child(id).child("Longitudine")
                    refLatitudine.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                             latitudine = dataSnapshot.value.toString()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    refLongitudine.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {


                             longitudine= dataSnapshot.value.toString()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                    preferitaBox.text= "Pizzeria preferita: "+preferita+" ,clicca per navigare"
                    preferitaBox.setOnClickListener(View.OnClickListener {
                        goTo(latitudine,longitudine)
                    })
                }
                else
                {
                    preferitaBox.text= "Pizzeria preferita: Non presente"
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


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

            R.id.buttonLogOut -> try {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("finish", true);
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                //FirebaseAuth.getInstance().signOut(); //bisogna gestire error di quando si torna indietro da smartphone
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun goTo(latitudine: String,longitudine: String) {

        val locationManager: LocationManager
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!


        val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                    "&saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr="+ latitudine + "," + longitudine +"&hl=zh&t=m&dirflg=d"))

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)

    }


}