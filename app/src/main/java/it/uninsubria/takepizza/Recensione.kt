package it.uninsubria.takepizza

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_recensione.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList


class Recensione : AppCompatActivity() {

    private var titolo :String=""
    private lateinit var auth: FirebaseAuth
    var databaseUsers: DatabaseReference? = null
    var databaseRecensioni: DatabaseReference? = null
    var database: FirebaseDatabase? = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recensione)
        val intent = intent
        titolo = intent.getStringExtra("titolo").toString()
        if(!titolo.contains("null"))
        {
            nomePizzeriaBox.setText(titolo)
        }


        auth = FirebaseAuth.getInstance()
        databaseUsers = database!!.getReference("Users");
        databaseRecensioni = database!!.getReference("recensioni");

    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.buttonMaps -> try {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            R.id.buttonAccount -> try {
                val intent = Intent(this, Account::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun write(v: View){
        var id: String = auth.getCurrentUser()!!.uid
        val username: DatabaseReference = databaseUsers!!.child(id).child("username")
        val myRef = FirebaseDatabase.getInstance().getReference("recensioni").child(id).push()
        myRef.child("stelle").setValue(stelleBox.getText().toString().trim())
        myRef.child("commento").setValue(commentoBox.getText().toString())
        myRef.child("pizzeria").setValue(nomePizzeriaBox.getText().toString())
        username.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.value.toString()
                myRef.child("username").setValue(username)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        Toast.makeText(this, "Recensione inviata", Toast.LENGTH_LONG).show()
    }
}