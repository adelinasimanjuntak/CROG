package com.example.loginemail_pages.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.loginemail_pages.FirstPage
import com.example.loginemail_pages.R
import com.google.firebase.database.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class HomeActivity : AppCompatActivity() {

    private val BARCODE_QR_READER_CODE = 505

    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val Btn = findViewById<Button>(R.id.btn_scan_code)
        Btn.setOnClickListener {
            val requestImageFromCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(requestImageFromCameraIntent, BARCODE_QR_READER_CODE)
        }

        val Back = findViewById<ImageView>(R.id.BACK)
        Back.setOnClickListener {
            val databaseReferenceKHUSUSLOGIN = FirebaseDatabase.getInstance().getReference("LOGIN")
            databaseReferenceKHUSUSLOGIN.removeValue()
            startActivity(Intent(applicationContext, FirstPage::class.java))
            finish()
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /// PROFILE PROGRAM
        var FullName = findViewById<TextView>(R.id.FullName)
        var Email = findViewById<TextView>(R.id.Email)
        var PhoneNumber = findViewById<TextView>(R.id.PhoneNumber)

        val email = findViewById<EditText>(R.id.email_login)
        var email1 = email.text.toString().trim()

        var database = FirebaseDatabase.getInstance()
        var databaseusers = database.getReference("Customers").child("users")
        var databaselogin = database.getReference("LOGIN").child("email1")

        var getProfile = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                        val name = i.child("nama1").getValue(String::class.java)
                        FullName.setText("$name")
                        val emael = i.child("email1").getValue(String::class.java)
                        Email.setText("$emael")
                        val telepon = i.child("phonenumber1").getValue(String::class.java)
                        PhoneNumber.setText("$telepon")
                }
            }
        }
        databaseusers.addValueEventListener(getProfile)
        databaseusers.addListenerForSingleValueEvent(getProfile)
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// QR CODE SCANNER PROGRAM
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            BARCODE_QR_READER_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = data?.extras!!.get("data") as Bitmap
                    qrCodeRecognoition(bitmap)
                }
            }
        }
    }

    private fun qrCodeRecognoition(picture: Bitmap){
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE)
                .build()

        val image = InputImage.fromBitmap(picture, 0)
        val detector = BarcodeScanning.getClient(options)

        detector.process(image)
                .addOnSuccessListener {
                    if(it.size == 1)
                    {
                        var anjay = findViewById<TextView>(R.id.txt_information)
                        anjay.text=""
                        anjay.text=it[0].rawValue

                        showEditTextBarcodeDialog()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error Occured While reading QR code", Toast.LENGTH_SHORT)
                            .show()
                }
    }

    private fun showToast(s: String) {
        Toast.makeText(this@HomeActivity, s, Toast.LENGTH_SHORT).show()
    }

    private fun showEditTextBarcodeDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogLayout = layoutInflater.inflate(R.layout.edit_text_layout, null)
        val editText: EditText = dialogLayout.findViewById<EditText>(R.id.et_editText)
        var regcode = findViewById<TextView>(R.id.regcode)

        with(builder){
            setTitle("Type Your Registration Code")
            setPositiveButton("OK"){ dialog, which->
                regcode.text = editText.text.toString().trim()
                if(regcode.text == "12345678"){
                    showToast("Welcome to Capsule 12")
                    startActivity(Intent(applicationContext, HomeActivity2::class.java))
                }
                else{
                    showToast("Sorry, the code has not been registered yet.")
                }
            }
            setNegativeButton("CANCEL"){ dialog, which->
                Log.d("Main", "Negative button clicked")
            }
            setView(dialogLayout)
            show()
        }
    }
}

