package com.example.gigglechat.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.gigglechat.MainActivity
import com.example.gigglechat.R
import com.example.gigglechat.databinding.ActivityMainBinding
import com.example.gigglechat.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
      private lateinit var binding:ActivitySignInBinding
      private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.continueBtn.setOnClickListener {

            if (binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter Your Number",Toast.LENGTH_SHORT).show()
            }
            else{
                var intent=Intent(this,OtpActivity::class.java)
                intent.putExtra("number",binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }

        }
    }
}