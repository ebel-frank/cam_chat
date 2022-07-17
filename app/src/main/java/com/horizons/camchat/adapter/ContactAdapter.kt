package com.horizons.camchat.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.horizons.camchat.databinding.ContactLayoutBinding
import com.horizons.camchat.model.ContactModel
import java.util.*

class ContactAdapter(private val contacts: List<ContactModel>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ContactLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    class ViewHolder(itemBinding: ContactLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val rnd = Random()
        private val contactIcon = itemBinding.contactIcon
        private val contactName = itemBinding.contactName
        private val contactDate = itemBinding.callInfo

        init {
            itemBinding.videoCall.setOnClickListener {

            }
        }

        fun bind(contact: ContactModel) {
            contactName.text = contact.name
            contactDate.text = contact.date
            val drawable: TextDrawable =
                TextDrawable.builder().buildRound(contact.name!!.substring(0, 1), Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)))
            contactIcon.setImageDrawable(drawable)

            when (contact.call_type) {
                0 -> {

                }
                1 -> {

                }
                2 -> {

                }
            }
        }

    }
}