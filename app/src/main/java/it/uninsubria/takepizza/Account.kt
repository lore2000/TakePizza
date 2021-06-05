package it.uninsubria.takepizza

import android.content.Intent
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = FirebaseAuth.getInstance()
        databaseUsers = database!!.getReference("Users");

        setUsrEmail()

    }


    fun setUsrEmail()
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


}