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
import com.example.project_auction.data.KakaoDTO
import com.example.project_auction.databinding.ActivityLoginBinding
import com.example.project_auction.util.http.HttpApi
import com.example.project_auction.view.activity.signup.SignUpActivity
import com.example.project_auction.viewmodel.LoginSignUpViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.Http
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel : LoginSignUpViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()
    private var googleSignInClient : GoogleSignInClient ?= null
    private val GOOGLE_LOGIN_CODE = 9001

    private val mOAuthLoginModule = OAuthLogin.getInstance()

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

    fun kakaoLogin(view : View){
        UserApiClient.instance.loginWithKakaoTalk(binding.root.context){
            token, error ->

            if (error != null){
                println("로그인 실패")
            }else if(token != null){
                println("로그인 성공")

                var kakaoDTO = KakaoDTO(token.accessToken)

                HttpApi().test(kakaoDTO).enqueue(object : Callback<KakaoDTO.KakaoResponse>{
                    override fun onResponse(
                        call: Call<KakaoDTO.KakaoResponse>,
                        response: Response<KakaoDTO.KakaoResponse>
                    ) {
                        println("-----------------------------" )
                        println("body = ${response.body()}")
                        println("code = ${response.body()?.code}")
                        println("data = ${response.body()?.data}")
                        println("msg = ${response.body()?.msg}")
                        println("-----------------------------")


                        FirebaseAuth.getInstance().signInWithCustomToken(response.body()?.data!!)
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    println("-----------------------------")
                                    println("signInWithCustomToken:success")
                                    println("${FirebaseAuth.getInstance().currentUser?.uid}")
                                    println("-----------------------------")
                                }else{
                                    println("signInWithCustomToken:fail")
                                    println("그아아아 ${it.addOnFailureListener { cause ->
                                        println("왜 실패? ${cause.toString()}")
                                    }}")
                                }
                            }.addOnFailureListener {
                                println("로그인 실패 ${it.toString()}")
                            }
                    }

                    override fun onFailure(call: Call<KakaoDTO.KakaoResponse>, t: Throwable) {
                        println(" 토큰 받아오기 실패 ")
                    }

                })
            }
        }
    }

    fun signUp(view : View){
        startActivity(Intent(binding.root.context,SignUpActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keyHash = Utility.getKeyHash(this)
        println("아아 키해쉬 입니다 $keyHash")

        //naver sdk 초기화
        mOAuthLoginModule.init(this,"D0nkJ2NHnNrr9qsuaobQ","oeQaBdgtSn","Auction")


        //kakao sdk 초기화
        KakaoSdk.init(binding.root.context, "a569b6cca45eb6678adf418f11dc4357")

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

    fun naver(view : View){
        val mOAuthHandler = object : OAuthLoginHandler(){
            override fun run(success: Boolean) {
                if (success){
                    val accessToken = mOAuthLoginModule.getAccessToken(binding.root.context)
                    val refreshToken = mOAuthLoginModule.getRefreshToken(binding.root.context)
                    val expireAt = mOAuthLoginModule.getExpiresAt(binding.root.context)
                    val tokenType = mOAuthLoginModule.getTokenType(binding.root.context)

                    println("네이버 로그인에 성공하였습니다." +
                            "\n $accessToken" +
                            "\n $refreshToken" +
                            "\n $expireAt" +
                            "\n $tokenType")
                }else{
                    println("네이버 로그인 실패")
                }
            }

        }

        mOAuthLoginModule.startOauthLoginActivity(this,mOAuthHandler)
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