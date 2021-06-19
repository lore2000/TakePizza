package it.uninsubria.takepizza

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_recensione.*


class Recensione : AppCompatActivity() {

    private var titolo :String=""

    var gender = arrayOf("1", "2","3","4","5")
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
        val spin: Spinner = findViewById(R.id.spinner);
        auth = FirebaseAuth.getInstance()
        databaseUsers = database!!.getReference("Users");
        databaseRecensioni = database!!.getReference("recensioni");

        val spin_adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender)
        spin.setAdapter(spin_adapter)

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
        val spin: Spinner = findViewById(R.id.spinner);
        val text: String = spin.getSelectedItem().toString()
        var id: String = auth.getCurrentUser()!!.uid
        val username: DatabaseReference = databaseUsers!!.child(id).child("username")
        if(nomePizzeriaBox.text.toString().isNotEmpty())
        {
            val myRef = FirebaseDatabase.getInstance().getReference("recensioni").child(id).child(nomePizzeriaBox.text.toString())
            myRef.child("stelle").setValue(text)
            myRef.child("commento").setValue(commentoBox.getText().toString())
            username.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.value.toString()
                    myRef.child("username").setValue(username)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            Toast.makeText(this, "Recensione inviata", Toast.LENGTH_LONG).show()
            finish()
        }
        else
        {
            nomePizzeriaBox.error = "Inserisci nome pizzeria"
            Toast.makeText(this, "Inserisci nome pizzeria", Toast.LENGTH_LONG).show()
        }

    }
}