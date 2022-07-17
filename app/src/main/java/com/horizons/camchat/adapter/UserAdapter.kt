package com.horizons.camchat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.horizons.camchat.databinding.UsersLayoutBinding
import com.horizons.camchat.model.UserModel
import java.util.*

class UserAdapter(private val users: List<UserModel>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            UsersLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    class ViewHolder(itemBinding: UsersLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val rnd = Random()
        private val contactIcon = itemBinding.contactIcon
        private val contactName = itemBinding.contactName
        private val contactUsername = itemBinding.contactUserName

        init {
            itemBinding.addUser.setOnClickListener {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: UserModel) {
            contactName.text = user.name
            contactUsername.text = "@${user.name?.split(" ")?.get(0)}"
            val drawable: TextDrawable =
                TextDrawable.builder().buildRound(user.name!!.substring(0, 1), Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
            contactIcon.setImageDrawable(drawable)
        }

    }
}