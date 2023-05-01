package com.himanshu.codes.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.himanshu.codes.R
import com.himanshu.codes.databinding.ScreenLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    private val key = "UID"
    private val name = "NAME"

    private lateinit var binding: ScreenLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ScreenLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(this, options)
        signInClient.signInIntent.also { intent ->
            getResult.launch(intent)
        }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data).result
                googleAuthForFirebase(account)
            }
        }

    private fun googleAuthForFirebase(account: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credential).await()
                withContext(Dispatchers.Main)
                {

                    val intent = Intent(applicationContext, HomeScreen::class.java)
                    intent.putExtra(key, auth.currentUser?.uid.toString())
                    intent.putExtra(name, auth.currentUser?.displayName.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        applicationContext,
                        "Login Failed\n${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}