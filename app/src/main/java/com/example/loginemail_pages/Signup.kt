package com.example.loginemail_pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.loginemail_pages.Activity.HomeActivity
//import com.example.loginemail_pages.Activity.HomeActivity2
import com.example.loginemail_pages.Model.Signup_model
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Signup : AppCompatActivity(), View.OnClickListener {
    lateinit var database : FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btn_alreadyaccount = findViewById<TextView>(R.id.btn_alreadyaccount)
        val btn_signup = findViewById<Button>(R.id.btn_signup)

        btn_alreadyaccount.setOnClickListener(this)
        btn_signup.setOnClickListener(this)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Customers").child("users")
    }


    override fun onClick(v: View?) {
        val btn_alreadyaccount = findViewById<TextView>(R.id.btn_alreadyaccount)
        val btn_signup = findViewById<Button>(R.id.btn_signup)

        when(v){
            btn_alreadyaccount -> {
                startActivity(Intent(applicationContext, Login::class.java))
            }
            btn_signup -> {
                signup()
            }
        }
    }


    private fun signup()
    {
        val nama = findViewById<EditText>(R.id.name_signup)
        val phonenumber = findViewById<EditText>(R.id.phone_signup)
        val email = findViewById<EditText>(R.id.email_signup)
        val password = findViewById<EditText>(R.id.password_signup)
        val confirmpassword = findViewById<EditText>(R.id.confirm_password_signup)

        var nama1 = nama.text.toString().trim()
        var phonenumber1 = phonenumber.text.toString().trim()
        var email1 = email.text.toString().trim()
        var password1 = password.text.toString().trim()
        var confirmpassword1 = confirmpassword.text.toString().trim()
        var checkphone = true
        checkphone = phonenumber1.matches("0+\\d+?".toRegex())


        if(nama1.isEmpty() || email1.isEmpty() ||  checkphone == false || password1.isEmpty() || confirmpassword1.isEmpty())
        {
            showToast("All fields required")
        }
        else
        {
            if(isValidEmail(email1))
            {
                var id = databaseReference.push().key
                var model = Signup_model(nama1, phonenumber1, email1, password1, id!!)

                databaseReference.child(id).setValue(model)
                showToast("Signup Successfull")
                startActivity(Intent(applicationContext,HomeActivity::class.java))
                finish()
            }
            else
            {
                showToast("Check your email Address")
            }
        }
    }

    private fun showToast(text:String)
    {
        Toast.makeText(this@Signup,text, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email1: String): Boolean
    {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email1).matches()
    }

    private fun isEmailExist(email1: String, password1 : String)
    {
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
                    showToast("You have already registered!")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }
        })
    }

}