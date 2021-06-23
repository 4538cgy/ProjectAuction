package com.example.project_auction.util.http

import com.example.project_auction.data.KakaoDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginToken {
    @Headers("Accept: application/json")
    @POST("/kakaoLogin")
    fun test(@Body kakaoTestDTO: KakaoDTO) : Call<KakaoDTO.KakaoResponse>
}