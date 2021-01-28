package com.example.loginemail_pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.loginemail_pages.Activity.HomeActivity
import com.example.loginemail_pages.Model.Login_model
import com.example.loginemail_pages.Model.Signup_model
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Login : AppCompatActivity() {
    lateinit var database : FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_create_new_account = findViewById<TextView>(R.id.btn_create_new_account)
        val btn_login = findViewById<Button>(R.id.btn_login)

        btn_create_new_account.setOnClickListener {
            startActivity(Intent(applicationContext, Signup::class.java))
        }
        btn_login.setOnClickListener{
            login()
        }

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Customers").child("users")
    }


    private fun login()
    {
        val email = findViewById<EditText>(R.id.email_login)
        val password = findViewById<EditText>(R.id.password_login)

        var email1 = email.text.toString().trim()
        var password1 = password.text.toString().trim()

        if(email1.isEmpty() || password1.isEmpty())
        {
            showToast("All fields required")
        }
        else
        {
            if(isValidEmail(email1))
            {
                isEmailExist(email1,password1)
            }
            else
            {
                showToast("Check your email Address")
            }
        }

    }

    private fun isEmailExist(email1: String, password1: String) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<Signup_model>()
                var isemailexist = false
                for (i in snapshot.children)
                {
                    var value = i.getValue<Signup_model>()

                    if(value!!.email1 == email1 && value!!.password1 == password1)
                    {
                        isemailexist = true
                    }
                    list.add(value!!)
                }


                if(isemailexist==true)
                {

                    var model = Login_model(email1)
                    var databaseReferenceKHUSUSLOGIN = database.getReference("LOGIN")
                    databaseReferenceKHUSUSLOGIN.setValue(model)
                    showToast("Login successfull \n Welcome to CROG Hotel Capsule")
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                }
                else
                {
                    showToast("Login failed! Check your email address/password")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }

    private fun isValidEmail(email1: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email1).matches()
    }

    private fun showToast(text: String) {
        Toast.makeText(this@Login, text, Toast.LENGTH_SHORT).show()
    }

}
