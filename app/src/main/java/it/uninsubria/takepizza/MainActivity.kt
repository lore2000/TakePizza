package it.uninsubria.takepizza

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private var mUserReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun isValidPassword(pass: String?): Boolean {
        return if (pass != null && pass.length >= 4) {
            true
        } else false
    }
    fun checkLogin(v: View?) {
        val username: String=userBox.getText().toString()
        val email: String = emailBox.getText().toString()
        if (!isValidEmail(email)) {
            //Set error message for email field
            emailBox.setError(getString(R.string.invalid_email))
        }
        val pass: String = pswBox.getText().toString()
        if (!isValidPassword(pass)) {
            //Set error message for password field
            pswBox.setError(getString(R.string.invalid_password))
        }
        val confpass : String = editTextConfermaPassword.getText().toString()
        if( pass!=confpass ){
            editTextConfermaPassword.setError(getString(R.string.confermainvalid_password))
        }else {
            if (isValidEmail(email) && isValidPassword(pass)) {
                val intent = Intent(this, MapsActivity::class.java)
                // intent.putExtra("keyIdentifier", value)
                val u=User(username,pass,email)
                mUserReference!!.child(username).setValue(u)
                startActivity(intent)
            }
        }
    }

    fun test(v: View?)
    {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}