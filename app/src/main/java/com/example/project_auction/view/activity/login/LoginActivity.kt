package com.example.project_auction.view.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.project_auction.R
import com.example.project_auction.base.BaseActivity
import com.example.project_auction.databinding.ActivityLoginBinding
import com.example.project_auction.viewmodel.LoginSignUpViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel : LoginSignUpViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()
    private var googleSignInClient : GoogleSignInClient ?= null
    private val GOOGLE_LOGIN_CODE = 9001

    private val googleLoginCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
        if (result != null) {
            if (result.isSuccess) {
                val account = result.signInAccount
                // second step
                firebaseAuthWithGoogle(account)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.apply {
            activitylogin = this@LoginActivity
            loginsignupviewmodel = viewModel
        }
    }

    fun google(view : View){
        val signInIntent = googleSignInClient?.signInIntent
        googleLoginCallback.launch(signInIntent)
        //startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_LOGIN_CODE) {
            // var -> val [result, account]
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result != null) {
                if (result.isSuccess) {
                    val account = result.signInAccount
                    // second step
                    firebaseAuthWithGoogle(account)
                }
            }
        }
    }

     */

    // Make private
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        // var -> val
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task -> // Remove safe-call
            if (task.isSuccessful) {
                // login
                moveMainPage(task.result?.user)
            } else {
                // show the error message
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {

            FirebaseFirestore.getInstance().collection("User").document("UserData")
                    .collection("userInfo").whereEqualTo("uid",auth.currentUser?.uid.toString())
                    .addSnapshotListener { documentSnapshot, _ -> // Remove useless parameter
                        if (documentSnapshot != null) {
                            if (!documentSnapshot.isEmpty) { // Remove NonNull
                                //startActivity(Intent(this, LobbyActivity::class.java))
                            } else {
                                //startActivity(Intent(this, SignUpActivity::class.java))
                            }
                            finish()
                        }
                    }
        }
    }
}