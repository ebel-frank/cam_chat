package com.horizons.camchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.horizons.camchat.databinding.NotificationLayoutBinding
import com.horizons.camchat.model.NotificationModel

class NotificationAdapter(private val notif: List<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NotificationLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notif[position])
    }

    override fun getItemCount(): Int = notif.size

    class ViewHolder(itemBinding: NotificationLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val message = itemBinding.message
        private val date = itemBinding.date

        init {
            itemBinding.delete.setOnClickListener {

            }
        }

        fun bind(notif: NotificationModel) {
            message.text = notif.message
            date.text = notif.date
        }

    }
}