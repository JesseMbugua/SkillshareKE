package com.example.skillshare

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameEt = findViewById<EditText>(R.id.etName)
        val emailEt = findViewById<EditText>(R.id.etEmail)
        val phoneEt = findViewById<EditText>(R.id.etPhone)
        val passwordEt = findViewById<EditText>(R.id.etPassword)
        val spinnerCounty = findViewById<Spinner>(R.id.spinnerCounty)
        val signUpBtn = findViewById<Button>(R.id.btnSignUp)
        val loginTv = findViewById<TextView>(R.id.tvLogin)


        val counties = arrayOf(
            "Select County",
            "Baringo", "Bomet", "Bungoma", "Busia", "Elgeyo Marakwet", "Embu",
            "Garissa", "Homa Bay", "Isiolo", "Kajiado", "Kakamega", "Kericho", "Kiambu",
            "Kilifi", "Kirinyaga", "Kisii", "Kisumu", "Kitui", "Kwale", "Laikipia",
            "Lamu", "Machakos", "Makueni", "Mandera", "Marsabit", "Meru", "Migori",
            "Mombasa", "Murang'a", "Nairobi", "Nakuru", "Nandi", "Narok", "Nyamira",
            "Nyandarua", "Nyeri", "Samburu", "Siaya", "Taita Taveta", "Tana River",
            "Tharaka Nithi", "Trans Nzoia", "Turkana", "Uasin Gishu", "Vihiga", "Wajir", "West Pokot"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, counties)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCounty.adapter = adapter

        signUpBtn.setOnClickListener {
            val name = nameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val phone = phoneEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val county = spinnerCounty.selectedItem.toString()

            when {
                name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() ->
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

                !phone.startsWith("+254") || phone.length != 13 ->
                    Toast.makeText(this, "Phone must start with +254 and be 13 chars (e.g. +254712345678)", Toast.LENGTH_LONG).show()

                county == "Select County" ->
                    Toast.makeText(this, "Please select a county", Toast.LENGTH_SHORT).show()

                else -> {
                    // TODO: save to backend (Firebase/REST). For now demo success:
                    Toast.makeText(this, "Account created (demo)", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        loginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
