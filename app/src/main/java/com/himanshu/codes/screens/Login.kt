package com.himanshu.codes.screens

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

    private lateinit var binding: ScreenLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var UID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScreenLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        login()
    }

    //enter home screen
    private fun launch() {
        val intent = Intent(applicationContext,HomeScreen::class.java)
        intent.putExtra("UID",UID)
        Toast.makeText(applicationContext,UID,Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {activityResult->
            if (activityResult.resultCode == Activity.RESULT_OK) {

                //Toast.makeText(applicationContext,"Got result",Toast.LENGTH_SHORT).show()

                val account = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data).result
                googleAuthForFirebase(account)
            }
        }

    private fun googleAuthForFirebase(account: GoogleSignInAccount?) {

        //Toast.makeText(applicationContext,"Google Auth for firebase",Toast.LENGTH_SHORT).show()

        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credential).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext,"Logged In",Toast.LENGTH_SHORT).show()
                    UID = auth.currentUser?.uid.toString()
                    launch()
                }
            }catch (e:Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext,"Login Failed\n${e.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun login() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        //Toast.makeText(applicationContext,"Button build",Toast.LENGTH_SHORT).show()
        val signInClient = GoogleSignIn.getClient(this,options)
        signInClient.signInIntent.also {intent->

            //Toast.makeText(applicationContext,"Sign-in Client",Toast.LENGTH_SHORT).show()

            getResult.launch(intent)
        }
    }
}