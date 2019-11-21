package com.example.day3

import com.example.day3.game1.Ans
import com.example.day3.wait.Data
import com.example.day3.wait.Enter
import com.example.day3.wait.EnterBackData
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @Headers("Content-Type: application/json","Accept: application/json")

    @GET("/api/playerGameStatus")
    fun getList(
    ): Call<Data>

    @POST("/api/enterGame")
    fun enter(
        @Body enter:Enter
    ): Call<EnterBackData>

    @GET("/api/playerGameAnswer/{playerId}")
    fun ans(
        @Path("playerId") playerid:String
    ): Call<Ans>

}
