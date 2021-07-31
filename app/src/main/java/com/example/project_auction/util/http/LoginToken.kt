package com.example.project_auction.util.http

import com.example.project_auction.data.*
import retrofit2.Call
import retrofit2.http.*

interface LoginToken {
    @Headers("Accept: application/json")
    @POST("/kakaoLogin")
    fun test(@Body kakaoTestDTO: KakaoDTO) : Call<KakaoDTO.KakaoResponse>

    @Headers("Accept: application/json")
    @POST("/naverLogin")
    fun test2(@Body naverTestDTO: NaverDTO) : Call<NaverDTO.NaverResponse>

    @Headers("Accept: application/json")
    @POST("/getServerTime")
    fun test3(@Body dto : TimeRequestDTO.Time) : Call<TimeRequestDTO.TimeResponse>

    @Headers("Accept: application/json")
    @GET("/acutionRead")
    fun getAuctionProduct(@Query("page") page : Int , @Query("orderBy") orderBy : Int , @Query("uid") uid : String , @Query("sortKey") sortkey : String) : Call<ProductAuctionDTO.ProductResponseDTO>

    @Headers("Accept: application/json")
    @GET("/tradeRead")
    fun getTradeProduct(@Query("page") page : Int , @Query("orderBy") orderBy: Int, @Query("uid") uid : String , @Query("sortKey") sortkey: String , @Query("endFlag") endFlag : Boolean) : Call<ProductTradeDTO.ProductResponseDTO>

    @Headers("Accept: application/json")
    @GET("/acutionRead2")
    fun getAuctionProduct2(@Query("page") page : Int , @Query("orderBy") orderBy : Int , @Query("uid") uid : String , @Query("sortKey") sortkey : String) : Call<ProductAuctionDTO.ProductResponseDTO>

}