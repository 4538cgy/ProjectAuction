package com.example.project_auction.util.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.project_auction.R
import com.example.project_auction.view.activity.login.LoginActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

class SignOut(private val context: Context, private val activity: Activity) : Activity() {

    var gac : GoogleApiClient? = null

    init {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        gac = GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
            .build()
    }

    fun signOut(){
        gac?.connect()

        gac?.registerConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(bundle: Bundle?) {
                FirebaseAuth.getInstance().signOut()
                if (gac!!.isConnected) {
                    Auth.GoogleSignInApi.signOut(gac).setResultCallback { status ->
                        if (status.isSuccess) {
                            Log.v("알림", "로그아웃 성공")

                            var intent = Intent(context,LoginActivity::class.java)
                            intent.apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                context.startActivity(intent)
                                activity.finish()
                            }



                            setResult(1)
                        } else {
                            setResult(0)
                        }
                    }
                }
            }

            override fun onConnectionSuspended(i: Int) {
                Log.v("알림", "Google API Client Connection Suspended")
                setResult(-1)
            }
        })


    }
}