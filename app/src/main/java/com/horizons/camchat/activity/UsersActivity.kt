package com.horizons.camchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.adapter.ContactAdapter
import com.horizons.camchat.adapter.UserAdapter
import com.horizons.camchat.databinding.ActivityUsersBinding
import com.horizons.camchat.model.ContactModel
import com.horizons.camchat.model.UserModel

class UsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val databaseRef = Firebase.database.getReference("users")

        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            val data = dataSnapshot.children.filter {
                it.key != Firebase.auth.currentUser?.uid
            }.map {
                val user = it.getValue(UserModel::class.java)!!
                user.user_id = it.key
                user
            }

            binding.recyclerView.adapter = UserAdapter(data)
        }

    }
}