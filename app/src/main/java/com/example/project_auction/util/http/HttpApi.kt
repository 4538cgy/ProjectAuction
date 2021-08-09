package com.example.project_auction.util.http

import com.example.project_auction.data.*
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

    fun test2(testDTO : NaverDTO) : Call<NaverDTO.NaverResponse>{
        return loginTokenInterface.test2(testDTO)
    }

    fun test3(testDTO : TimeRequestDTO.Time) : Call<TimeRequestDTO.TimeResponse>
    {
        return loginTokenInterface.test3(testDTO)
    }

    fun getAuctionProduct(page : Int , orderBy : Int , uid : String , sortKey : String,category : String) : Call<ProductAuctionDTO.ProductResponseDTO>
    {
        return loginTokenInterface.getAuctionProduct(page,orderBy,uid,sortKey,category)
    }

    fun getTradeProduct(page : Int , orderBy: Int , uid : String, sortKey: String , endFlag : Boolean,category : String) : Call<ProductTradeDTO.ProductResponseDTO>
    {
        return loginTokenInterface.getTradeProduct(page,orderBy,uid,sortKey,endFlag,category)
    }

    fun getAuctionProduct2(page : Int , orderBy : Int , uid : String , sortKey : String) : Call<ProductAuctionDTO.ProductResponseDTO>
    {
        return loginTokenInterface.getAuctionProduct2(page,orderBy,uid,sortKey)
    }
}