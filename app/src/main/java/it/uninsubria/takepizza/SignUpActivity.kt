package it.uninsubria.takepizza

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        registerButton.setOnClickListener{
            onSignUpClick()
        }
        loginTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }





    private fun onSignUpClick() {
        val email = emailBox.text.toString().trim()
        val password = pswBox.text.toString().trim()
        val userName = userBox.text.toString().trim()
        if (userName.isEmpty()) {
            userBox.error = "Enter userName"
            return
        }
        if (email.isEmpty()) {
           emailBox.error = "Enter email"
            return
        }
        if (password.isEmpty()) {
            pswBox.error = "Enter password"
            return
        }
        createUser(userName, email, password)
    }

    private fun createUser(userName: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


// Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val currenyUser = auth.currentUser
                    val uid = currenyUser!!.uid
                    val userMap = HashMap<String, String>()
                    userMap["username"] = userName
                    val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                    database.setValue(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(applicationContext, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
// If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }

    }