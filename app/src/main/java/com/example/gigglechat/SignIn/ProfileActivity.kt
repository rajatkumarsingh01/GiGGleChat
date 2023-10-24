package com.example.gigglechat.SignIn

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gigglechat.MainActivity
import com.example.gigglechat.databinding.ActivityProfileBinding
import com.example.gigglechat.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private lateinit var selectedImg:Uri
    private lateinit var selectedCoverImg:Uri

    private lateinit var dialog: AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog=AlertDialog.Builder(this)
            .setMessage("Updating Profile")
            .setCancelable(false)

        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()
        auth=FirebaseAuth.getInstance()




        //to get image from gallery for profile

        binding.coverImage.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,1)

        }
        binding.userImage.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,2)

        }
        binding.continueMainBtn.setOnClickListener {
            if (binding.userName.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter your name",Toast.LENGTH_SHORT).show()

            }
            else if(selectedImg==null){
                Toast.makeText(this,"Select the user Image",Toast.LENGTH_SHORT).show()

            }
            else if (selectedCoverImg==null){
                Toast.makeText(this,"Please choose the cover photo",Toast.LENGTH_SHORT).show()
            }
            else uploadData()

        }

    }

    private fun uploadData() {
        //user's data stored in firebase
        //for userimage upload
        val userImagereference=storage.reference.child("Profile").child(Date().time.toString())
        userImagereference.putFile(selectedImg).addOnCompleteListener{
            userImagetask->
            if(userImagetask.isSuccessful){
                userImagereference.downloadUrl.addOnCompleteListener {
                    userImagetask->
                    val userImageUrl = userImagetask.result.toString()

                    //for cover Image Upload
                    val coverImagereference=storage.reference.child("Profile").child(Date().time.toString())
                    coverImagereference.putFile(selectedCoverImg).addOnCompleteListener { coverImagetask ->
                        if (coverImagetask.isSuccessful) {
                            userImagereference.downloadUrl.addOnCompleteListener { userImagetask ->
                                val coverImageUrl = coverImagetask.result.toString()

                                uploadInfo(userImageUrl,coverImageUrl)
                            }
                        }
                    }

                }
            }
        }
    }

    private fun uploadInfo(userimgUrl: String, coverImgUrl: String) {
        val user=UserModel(auth.uid.toString(),binding.userName.text.toString(),
            auth.currentUser?.phoneNumber.toString(),userimgUrl,coverImgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Data Inserted",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()


            }




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data !=null){
            if (data.data!=null){
               if(requestCode==1){
                   selectedCoverImg=data.data!!
                   binding.coverImage.setImageURI(selectedCoverImg)
               }
                else if(requestCode==2){
                    selectedImg=data.data!!
                   binding.userImage.setImageURI(selectedImg)

                }
            }
        }
    }
}