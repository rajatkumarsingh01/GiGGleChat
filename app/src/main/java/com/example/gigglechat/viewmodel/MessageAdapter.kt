package com.example.gigglechat.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.gigglechat.R
import com.example.gigglechat.databinding.ReciverItemLayoutBinding
import com.example.gigglechat.databinding.SentItemLayoutBinding
import com.example.gigglechat.model.MessageModel
import com.google.firebase.auth.FirebaseAuth



class MessageAdapter(var context: Context,var list: ArrayList<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var Item_Sent=1
    var Item_Receive=2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        //here we will get if it is sender they will send and use sent layout or receiver will use receive layout
        return if(viewType==Item_Sent)
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        else   ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.reciver_item_layout,parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        //this will check for the senderId and compare thatb the sender is current user or not
        return if(FirebaseAuth.getInstance().uid==list[position].senderId)  Item_Sent else Item_Receive


    }

    override fun getItemCount(): Int {

       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //here we will bind the mesages to textView
       val message=list[position]
        if(holder.itemViewType==Item_Sent){
            val viewHolder=holder as SentViewHolder
            viewHolder.binding.userMessage.text=message.message
        }
        else{
            val viewHolder=holder as ReceiverViewHolder
            viewHolder.binding.userMessage.text=message.message

        }
    }

    inner class SentViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding=SentItemLayoutBinding.bind(view)

    }

    inner class ReceiverViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding= ReciverItemLayoutBinding.bind(view)

    }
}