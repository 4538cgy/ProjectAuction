package com.example.project_auction.util.http

import com.example.project_auction.data.KakaoDTO
import com.example.project_auction.data.NaverDTO
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
}