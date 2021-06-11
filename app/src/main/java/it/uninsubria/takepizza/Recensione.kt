package it.uninsubria.takepizza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_recensione.*
import kotlinx.android.synthetic.main.activity_signup.*


class Recensione : AppCompatActivity() {
    private val TAG = "Recensione"
    private var mReviewsReference: DatabaseReference? =FirebaseDatabase.getInstance().getReference("recensioni")
    private val mReviews: MutableList<SaveReviews> = ArrayList()
    lateinit private var mReviewChildListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recensione)

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
    fun writeRecensione(v: View){
        var com = commentoBox.getText().toString()
        var stelle = stelleBox.getText().toString()
        val nome = usernameBox.getText().toString()
        SaveReviews("",com,stelle)
        Toast.makeText(this@Recensione, nome, Toast.LENGTH_SHORT).show()

    }
    override fun onStart() {
        super.onStart()
        val usersChildListener = getUsersChildEventListener()
        mReviewsReference!!.addChildEventListener(usersChildListener)
        mReviewChildListener = usersChildListener
    }

    override fun onStop() {
        super.onStop()
        if (mReviewChildListener != null) {
            mReviewsReference!!.removeEventListener(mReviewChildListener)
        }
    }

    fun getUsersChildEventListener(): ChildEventListener{
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newReviews = dataSnapshot.getValue(SaveReviews::class.java)
                mReviews.add(newReviews!!)
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newReviews = dataSnapshot.getValue(SaveReviews::class.java)
                val reviewsKey = dataSnapshot.key
                mReviews.find { e -> e.toString().equals(reviewsKey) }?.set(newReviews!!)
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val reviewsKey = dataSnapshot.key
                var fu = mReviews.find { e -> e.toString().equals(reviewsKey) }
                mReviews.remove(fu)
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(this@Recensione, "Failed to load comments.", Toast.LENGTH_SHORT).show()
            }
        }
        return childEventListener
    }

}