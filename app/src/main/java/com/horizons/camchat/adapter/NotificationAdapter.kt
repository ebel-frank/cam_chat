package com.horizons.camchat.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.R
import com.horizons.camchat.databinding.NotificationLayoutBinding
import com.horizons.camchat.model.NotificationModel


class NotificationAdapter(private var notifications: MutableList<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size

    inner class ViewHolder(itemBinding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val message = itemBinding.message
        private val date = itemBinding.date

        init {
            itemBinding.delete.setOnClickListener {
                deleteNotification(notifications[adapterPosition].id!!)
            }
        }

        private fun deleteNotification(id: String) {
            notifications.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            val databaseRef = Firebase.database.getReference("notifications")
                .child(Firebase.auth.currentUser?.uid!!)
            databaseRef.child(id).removeValue()
        }

        fun bind(notification: NotificationModel) {
            message.makeLink(notification.message!!) {
                deleteNotification(notification.id!!)
            }
            date.text = notification.date
        }
    }
}

fun TextView.makeLink(text: String, onClick: () -> Unit) {

    val ss = SpannableString(text)
    val i1 = text.indexOf("Accept")
    val i2 = text.indexOf("Decline")
    val acceptSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            Toast.makeText(this@makeLink.context, "Invite Accepted", Toast.LENGTH_SHORT).show()
            onClick()
        }
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = this@makeLink.context.getColor(R.color.purple_500)
        }
    }
    // i2 + 6; I added 6 cause it's the length of the text "Accept"
    ss.setSpan(acceptSpan, i1, i1+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    val declineSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            Toast.makeText(this@makeLink.context, "Invite Declined", Toast.LENGTH_SHORT).show()
            onClick()
        }
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = this@makeLink.context.getColor(R.color.purple_500)
        }
    }
    // i2 + 7; I added 7 cause it's the length of the text "Decline"
    ss.setSpan(declineSpan, i2, i2+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.text = ss
}