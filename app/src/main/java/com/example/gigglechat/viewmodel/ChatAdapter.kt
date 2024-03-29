package com.example.gigglechat.viewmodel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gigglechat.ChatActivity
import com.example.gigglechat.R
import com.example.gigglechat.databinding.ChatUserItemLayoutBinding
import com.example.gigglechat.model.UserModel

class ChatAdapter(var context: Context,var list:ArrayList<UserModel>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding:ChatUserItemLayoutBinding=ChatUserItemLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

        return ChatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_user_item_layout,parent,false))

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var user=list[position]
        Glide.with(context).load(user.userimgUrl).into(holder.binding.userImage)
        holder.binding.userName.text=user.name

        holder.itemView.setOnClickListener {
            val intent= Intent(context,ChatActivity::class.java)
            intent.putExtra("uid",user.uid)
             context.startActivity(intent)
        }

    }

}