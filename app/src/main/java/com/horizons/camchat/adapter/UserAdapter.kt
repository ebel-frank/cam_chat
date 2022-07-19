package com.horizons.camchat.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.databinding.UsersLayoutBinding
import com.horizons.camchat.model.UserModel
import java.util.*

class UserAdapter(private val users: MutableList<UserModel>) :
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

    inner class ViewHolder(itemBinding: UsersLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val rnd = Random()
        private val contactIcon = itemBinding.contactIcon
        private val contactName = itemBinding.contactName
        private val contactUsername = itemBinding.contactUserName

        init {
            itemBinding.connect.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Invite sent to ${users[adapterPosition].name}",
                    Toast.LENGTH_SHORT
                ).show()
                val databaseRef = Firebase.database
                databaseRef.getReference("connections")
                    .child(FirebaseAuth.getInstance().currentUser?.uid!!).push()
                    .setValue(
                        mapOf(
                            "call_type" to rnd.nextInt(3),
                            "date" to "12-12-2012",
                            "name" to users[adapterPosition].name!!,
                            "user_id" to users[adapterPosition].user_id!!,
                        )
                    )
                databaseRef.getReference("notifications").child(users[adapterPosition].user_id!!)
                    .push()
                    .setValue(
                        mapOf(
                            "date" to "12-12-2012",
                            "message" to "Request: @${
                                Firebase.auth.currentUser?.displayName?.split(" ")?.get(0)
                                    ?.lowercase()
                            } sent you a connection request. Accept or Decline",
                            "user_id" to Firebase.auth.currentUser?.uid
                        )
                    )
                users.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: UserModel) {
            contactName.text = user.name
            contactUsername.text = "@${user.name?.split(" ")?.get(0)?.lowercase()}"
            val drawable: TextDrawable =
                TextDrawable.builder().buildRound(
                    user.name!!.substring(0, 1),
                    Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                )
            contactIcon.setImageDrawable(drawable)
        }

    }
}