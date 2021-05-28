package it.uninsubria.takepizza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var mUserReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("users")
    private val mUsers: MutableList<User> = ArrayList()
    private lateinit var mUsersChildListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewSingUp.setOnClickListener{
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }
    }
    fun accesso(v: View?)
    {
        val email: String= emailEditText.getText().toString()
        val password:String=passwordEditText.getText().toString()
        val verifica:String= "$email-$password"
        val t: User? =mUsers.find { e -> e.toString().equals(verifica) }
        if (t != null && t.toString().equals(verifica)) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this@MainActivity, "Email o password sbagliata", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val usersChildListener = getUsersChildEventListener()
        mUserReference!!.addChildEventListener(usersChildListener)
        mUsersChildListener = usersChildListener
    }
    override fun onStop() {
        super.onStop()
        if (mUsersChildListener != null) {
            mUserReference!!.removeEventListener(mUsersChildListener)
        }
    }
    fun getUsersChildEventListener(): ChildEventListener{ // metodo usato per tenere aggiornato il login in caso si aggiunga un nuovo utente
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newUser = dataSnapshot.getValue(User::class.java)
                mUsers.add(newUser!!)
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newUser = dataSnapshot.getValue(User::class.java)
                val userKey = dataSnapshot.key
                mUsers.find { e -> e.toString().equals(userKey) }?.set(newUser!!)
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val userKey = dataSnapshot.key
                var fu = mUsers.find { e -> e.toString().equals(userKey) }
                mUsers.remove(fu)
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(this@MainActivity, "Failed to load comments.", Toast.LENGTH_SHORT).show()
            }
        }
        return childEventListener
    }

}