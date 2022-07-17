package com.horizons.camchat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.adapter.NotificationAdapter
import com.horizons.camchat.databinding.ActivityNotificationBinding
import com.horizons.camchat.model.NotificationModel

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val databaseRef = Firebase.database.getReference("notifications")

        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            val data = dataSnapshot.children.filter {
                it.key != Firebase.auth.currentUser?.uid
            }.map {
                val notification = it.getValue(NotificationModel::class.java)!!
                notification.id = it.key
                notification
            }

            binding.recyclerView.adapter = NotificationAdapter(data)
        }

    }
}