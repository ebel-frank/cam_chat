package com.horizons.camchat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.adapter.ContactAdapter
import com.horizons.camchat.databinding.ActivityContactBinding
import com.horizons.camchat.model.ContactModel
import com.horizons.camchat.viewmodel.ContactViewModel
import com.horizons.camchat.viewmodel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.notification.setOnClickListener {
            Intent(this, NotificationActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.addUsers.setOnClickListener {
            Intent(this, UsersActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.searchBar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val data = dataSnapshot.children.map {
                    it.getValue(ContactModel::class.java)!!
                }
                val adapter = (binding.recyclerView.adapter as ContactAdapter?)
                if (data.size != adapter?.contacts?.size) {
                    adapter?.setData(data)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(
                    baseContext, databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val databaseRef = Firebase.database.getReference("connections")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!)

        databaseRef.addValueEventListener(postListener)
        databaseRef.get().addOnSuccessListener { dataSnapshot ->
            val data = dataSnapshot.children.map {
                it.getValue(ContactModel::class.java)!!
            }

            binding.recyclerView.adapter = ContactAdapter(data)
        }
        databaseRef.addValueEventListener(postListener)
    }

}