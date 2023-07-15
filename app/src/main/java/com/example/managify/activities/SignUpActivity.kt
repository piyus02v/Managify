package com.example.managify.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.example.managify.R
import com.example.managify.firebase.FirestoreHandler
import com.example.managify.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setActionBar()


        val sign_up_page_btn: Button = findViewById(R.id.sign_up_page_btn)
        sign_up_page_btn.setOnClickListener {
            registerUser()

        }
    }
    private fun setActionBar() {
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_sign_up_activity))
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        findViewById<Toolbar>(R.id.toolbar_sign_up_activity).setNavigationOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        val name = findViewById<AppCompatEditText>(R.id.name_et).text.toString().trim { it <= ' ' } //remove spaces
        val email = findViewById<AppCompatEditText>(R.id.email_et).text.toString().trim { it <= ' ' }
        val password = findViewById<AppCompatEditText>(R.id.password_et).text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            showProgressDialog()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //send user details for user creation in firebase
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, registeredEmail)
                        FirestoreHandler().registerUser(this,user)
                    } else {
                        Toast.makeText(
                            this,
                            "Registration Failed. Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this,
            "You have successfully registered",
            Toast.LENGTH_SHORT).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }
}