package com.example.loginemail_pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.loginemail_pages.Activity.HomeActivity

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        val login_button = findViewById<Button>(R.id.LOGIN_FIRSTPAGE)
        val register_button = findViewById<Button>(R.id.REGISTER_FIRSTPAGE)

        login_button.setOnClickListener{
            startActivity(Intent(applicationContext, Login::class.java))
        }

        register_button.setOnClickListener{
            startActivity(Intent(applicationContext, Signup::class.java))
        }
    }
}