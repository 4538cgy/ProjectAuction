package com.example.project_auction.util.http

import com.example.project_auction.data.KakaoDTO
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpApi {

    private val APITAG : String = "RestApi"
    private val loginTokenInterface : LoginToken
    private val baseUrl : String = "https://asia-northeast3-auction-12e08.cloudfunctions.net"

    init{
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient())
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        loginTokenInterface = retrofit.create(LoginToken::class.java)
    }


    fun test(testDTO: KakaoDTO) : Call<KakaoDTO.KakaoResponse>{
        return loginTokenInterface.test(testDTO)
    }
}