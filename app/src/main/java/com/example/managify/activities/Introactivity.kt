package com.example.managify.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.managify.R

class introactivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introactivity)

        val sign_up_btn:Button=findViewById(R.id.sign_up_btn)
        sign_up_btn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        val sign_in_btn:Button=findViewById(R.id.sign_in_btn)
        sign_in_btn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}