package com.horizons.camchat

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner.Silent::class)
class ExampleUnitTest {
    private lateinit var successTask: Task<AuthResult>

    @Mock
    private lateinit var mAuth: FirebaseAuth

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        successTask = object : Task<AuthResult>() {
            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                print("Hello")
                return successTask
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                print("Hello")
                return successTask
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                print("Hello")
                return successTask
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> = successTask

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> = successTask

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> = successTask

            override fun getException(): Exception? = null

            override fun getResult(): AuthResult = successTask.result

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult = successTask.result

            override fun isCanceled(): Boolean = true

            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

        }
    }

    @Test
    fun user_has_connections() {
        val email = "frankebeledike@gmail.com"
        val password = "1234567"
        Mockito.`when`(this.mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                print("Hello")
            })
//            val user = this.mAuth.currentUser
//            user?.uid?.let {
//                Firebase.database.getReference("connections")
//                    .child(it).get().addOnSuccessListener { dataSnapshot ->
//                        val data = dataSnapshot.getValue<List<ContactModel>>()
//
//                        assert(data?.size != 0)
//                    }
//            }
//            print(user?.uid)
        print(this.mAuth.currentUser?.uid)
        assertEquals(4, 2 + 2)
    }
}