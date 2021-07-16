package com.example.project_auction.util.http

import com.example.project_auction.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginToken {
    @Headers("Accept: application/json")
    @POST("/kakaoLogin")
    fun test(@Body kakaoTestDTO: KakaoDTO) : Call<KakaoDTO.KakaoResponse>

    @Headers("Accept: application/json")
    @POST("/naverLogin")
    fun test2(@Body naverTestDTO: NaverDTO) : Call<NaverDTO.NaverResponse>

    @Headers("Accept: application/json")
    @POST("/getServerTime")
    fun test3(@Body dto : TimeRequestDTO.Time) : Call<TimeRequestDTO.data>


    @Headers("Accept: application/json")
    @POST("/AuctionRead")
    fun getAuctionProduct(@Body postAuction : PostRequestDTO) : Call<ProductAuctionDTO>
}