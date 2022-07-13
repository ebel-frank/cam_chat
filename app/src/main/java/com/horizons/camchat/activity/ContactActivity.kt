package com.horizons.camchat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horizons.camchat.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}