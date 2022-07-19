package com.horizons.camchat.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.horizons.camchat.databinding.ActivityLoginBinding
import com.horizons.camchat.viewmodel.LoginViewModel
import com.google.firebase.auth.UserProfileChangeRequest




class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = binding.name
        val email = binding.email
        val password = binding.password
        val login = binding.login
        val register = binding.register
        val loading = binding.loading

        auth = Firebase.auth
        database = Firebase.database

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid && !loginState.activateRegister
            register.isEnabled = loginState.isDataValid && loginState.activateRegister

            Log.d("TAG", "isDataValid: ${loginState.isDataValid}, activateRegister: ${loginState.activateRegister}")

            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        name.afterTextChanged {
            loginViewModel.loginDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            loginViewModel.loginDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        startActivity(Intent(this, ContactActivity::class.java))
                        Toast.makeText(
                            baseContext, "Welcome ${Firebase.auth.currentUser?.displayName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    loading.visibility = View.GONE
                }
        }

        register.setOnClickListener {
            loading.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration Successful
                        database.getReference("users").child(auth.currentUser?.uid!!)
                            .setValue(mapOf("name" to name.text.toString()))
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name.text.toString()).build()
                        auth.currentUser?.updateProfile(profileUpdates)
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // If registration fails, display a message to the user.
                        Toast.makeText(
                            baseContext, task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    loading.visibility = View.GONE
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, ContactActivity::class.java))
            finishAffinity()
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

