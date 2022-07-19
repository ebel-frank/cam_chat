package com.horizons.camchat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        val databaseRef = Firebase.database

        databaseRef.getReference("users").get().addOnSuccessListener { dataSnapshot ->
            val uid = Firebase.auth.currentUser?.uid
            val data = dataSnapshot.children.filter {
                it.key != uid
            }.map {
                val user = it.getValue(UserModel::class.java)!!
                user.user_id = it.key
                user
            }
            data as MutableList<UserModel>

            databaseRef.getReference("connections").child(uid!!).get().addOnSuccessListener { snapshot ->
                snapshot.children.forEach {
                    val connection = it.getValue(ContactModel::class.java)!!
                    data.removeIf { user ->
                        user.user_id == connection.user_id
                    }
                }
                binding.recyclerView.adapter = UserAdapter(data)
            }

        }
    }
}