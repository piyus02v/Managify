package com.example.managify.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.managify.R
import com.example.managify.firebase.FirestoreHandler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler().postDelayed({
            var currentUserID = FirestoreHandler().getCurrentUserId()
            if(currentUserID.isNotEmpty()){
                //auto login - go straight to the main activity
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                // Start the Login Activity
                startActivity(Intent(this, introactivity::class.java))
            }
            finish()
        },2500)
    }
}