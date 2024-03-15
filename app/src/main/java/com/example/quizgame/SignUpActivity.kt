package com.example.quizgame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {
    lateinit var signUpBinding: ActivitySignUpBinding
    val auth: FirebaseAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding= ActivitySignUpBinding.inflate(layoutInflater)
        val view=signUpBinding.root
        setContentView(view)
        signUpBinding.buttonsignup.setOnClickListener {
            val email=signUpBinding.editTextSignUpEmail.editText.toString()
            val password=signUpBinding.editTextSignUpPassword.editText.toString()
            signupwithfirebase(email,password)

        }
    }
    fun signupwithfirebase(email: String, password: String){
        signUpBinding.progressbarsignup.visibility= View.VISIBLE
        signUpBinding.buttonsignup.isClickable=false
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"your account has been created",Toast.LENGTH_SHORT).show()
                finish()
                signUpBinding.progressbarsignup.visibility=View.VISIBLE
                signUpBinding.buttonsignup.isClickable=true
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }
}