package com.example.managify.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.managify.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressOnce = false

    public lateinit var mProgressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.progress_dialog)
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        if(this::mProgressDialog.isInitialized)
        mProgressDialog.dismiss()
    }

    fun getCurrentUserID() : String {
        return FirebaseAuth.getInstance().currentUser!! .uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressOnce = true
        Toast.makeText(this,"Please press back once again to exit", Toast.LENGTH_LONG).show()

        Handler().postDelayed({doubleBackToExitPressOnce = false}, 2000)
    }

    fun showErrorSnackBar(message : String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.allcolor))
        snackBar.show()
    }
}