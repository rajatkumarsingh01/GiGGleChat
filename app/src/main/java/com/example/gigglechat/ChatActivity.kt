package com.example.gigglechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import com.example.gigglechat.databinding.ActivityChatBinding
import com.example.gigglechat.model.MessageModel
import com.example.gigglechat.viewmodel.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid:String
    private lateinit var receiverUid:String

    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String

    private lateinit var list:ArrayList<MessageModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= FirebaseDatabase.getInstance()
//getting sender  user id
        senderUid=FirebaseAuth.getInstance().uid.toString()
//getting receiver user Id
        receiverUid=intent.getStringExtra("uid")!!

        list=ArrayList()

        //both sender and receiver in room to save the data

        senderRoom=senderUid+receiverUid
        receiverRoom=receiverUid+senderUid




        binding.sendButton.setOnClickListener {
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this,"Type your message",Toast.LENGTH_SHORT).show()

            }
            else{

                val message=MessageModel(binding.messageBox.text.toString(),senderUid,Date().time)

                val randomKey=database.reference.push().key //user chat key generated
                  //all messages will be saved
                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {


                        database.reference.child("chats").child(receiverRoom).child("message")
                            .child(randomKey!!).setValue(message).addOnSuccessListener {

                                binding.messageBox.text=null
                                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()

                                
                            }
                    }


            }
        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for(snapshot1 in snapshot.children){
                        val data =snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    binding.recyclerViewChat.adapter=MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error:$error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}