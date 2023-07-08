package com.example.managify.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
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

//private lateinit var auth: FirebaseAuth
//
//class SignInActivity : BaseActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        auth = FirebaseAuth.getInstance()
//        val sign_in_page_btn : Button =findViewById(R.id.sign_in_page_btn)
//        sign_in_page_btn.setOnClickListener {
//            signInUser()
//        }
//    }
//    fun signInSuccess(user : User){
//        hideProgressDialog()
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
//
//
//
//    private fun signInUser(){
//        val email = findViewById<AppCompatEditText>(R.id.sign_in_email_et).text.toString().trim { it <= ' '}
//        val password = findViewById<AppCompatEditText>(R.id.sign_in_password_et).text.toString().trim { it <= ' '}
//
//        if(validateForm(email, password)) {
//            showProgressDialog()
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    hideProgressDialog()
//                    if (task.isSuccessful) {
//                        val user=auth.currentUser
//                        startActivity(Intent(this,MainActivity::class.java))
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(baseContext,
//                            "Authentication failed.",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//        }
//    }
//
//    private fun validateForm(email:String, password:String) : Boolean{
//        return when {
//            TextUtils.isEmpty(email) -> {
//                showErrorSnackBar("Please enter an email address")
//                false
//            }
//            TextUtils.isEmpty(password) -> {
//                showErrorSnackBar("Please enter a password")
//                false
//            }else->{
//                true
//            }
//        }
//    }
//
//}






private lateinit var auth: FirebaseAuth

class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setActionBar()


        auth = FirebaseAuth.getInstance();

        val sign_in_page_btn : Button =findViewById(R.id.sign_in_page_btn)
        sign_in_page_btn.setOnClickListener {
           signInUser()
        }
    }

    fun signInSuccess(user : User){
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun signInUser(){
        val email = findViewById<AppCompatEditText>(R.id.sign_in_email_et).text.toString().trim { it <= ' '}
        val password = findViewById<AppCompatEditText>(R.id.sign_in_password_et).text.toString().trim { it <= ' '}

        if(validateForm(email, password)) {
            showProgressDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        FirestoreHandler().loadUserData(this)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun validateForm(email:String, password:String) : Boolean{
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email address")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }else->{
                true
            }
        }
    }



    private fun setActionBar(){
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_sign_in_activity))
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        findViewById<Toolbar>(R.id.toolbar_sign_in_activity).setNavigationOnClickListener{onBackPressed()}
    }
}