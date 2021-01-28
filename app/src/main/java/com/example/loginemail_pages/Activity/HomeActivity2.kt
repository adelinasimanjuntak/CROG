package com.example.loginemail_pages.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginemail_pages.FirstPage
import com.example.loginemail_pages.Login
import com.example.loginemail_pages.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)
        var database = FirebaseDatabase.getInstance()
        var databasesuhu = database.getReference("ROOM TEMPERATURE")

        val textView = findViewById<TextView>(R.id.nilaisuhu)
        val door = findViewById<Switch>(R.id.switch1)
        val lampu = findViewById<Switch>(R.id.switch2)
        val profile = findViewById<ImageView>(R.id.profile)
        val logout = findViewById<ImageView>(R.id.logout)

        //////////////////////////////////////////////////////////////////////////////////
        // KLIK ICON PROFILE
        profile.setOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }

        //////////////////////////////////////////////////////////////////////////////////
        // KLIK ICON BACK LOGOUT
        logout.setOnClickListener {
            startActivity(Intent(applicationContext, FirstPage::class.java))
        }

        //////////////////////////////////////////////////////////////////////////////////
        //READ TEMPERATURE
        var getdatasuhu = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var suhuuu = snapshot.getValue()
                textView.setText("$suhuuu")
            }
        }
        databasesuhu.addValueEventListener(getdatasuhu)
        databasesuhu.addListenerForSingleValueEvent(getdatasuhu)

        //////////////////////////////////////////////////////////////////////////////////
        // LOCK-UNLOCK PINTU
        door.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("DOOR LOCK")

                myRef.setValue(1)
            }
            else {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("DOOR LOCK")

                myRef.setValue(0)
            }
        }

        //////////////////////////////////////////////////////////////////////////////////
        // LAMPU
        lampu.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("LAMP STATUS")

                myRef.setValue(1)
            }
            else {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("LAMP STATUS")

                myRef.setValue(0)
            }
        }
    }
}