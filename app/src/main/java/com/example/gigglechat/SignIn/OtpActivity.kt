package com.example.gigglechat.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gigglechat.R
import com.example.gigglechat.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOtpBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var verificationId:String
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        val builder=AlertDialog.Builder(this)

        builder.setMessage("Please Wait")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog=builder.create()
        dialog.show()

        val phoneNumber ="+91"+intent.getStringExtra("number")

        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OtpActivity,"Please Try Again",Toast.LENGTH_SHORT).show()

                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId=p0
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.verifyButton.setOnClickListener {
            if (binding.otpCode.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_SHORT).show()

            }
            else{
                dialog.show()
                val credential=PhoneAuthProvider.getCredential(verificationId,binding.otpCode.text!!.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            dialog.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()

                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }

        binding.resendOtp.setOnClickListener {
            Toast.makeText(this,"Sending Again",Toast.LENGTH_SHORT).show()

        }




    }
}