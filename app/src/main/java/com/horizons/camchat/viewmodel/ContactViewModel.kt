package com.horizons.camchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.model.ContactModel
import kotlinx.coroutines.launch

class ContactViewModel: ViewModel() {

    private val _contacts = MutableLiveData<List<ContactModel>>()
    val contactsList : LiveData<List<ContactModel>> = _contacts

    fun getContacts() {
        viewModelScope.launch {

        }
    }

}