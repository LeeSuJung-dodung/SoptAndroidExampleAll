package com.example.hw2_sopt.network

import com.example.hw2_sopt.data.RequestLogin
import com.example.hw2_sopt.data.RequestRegister
import com.example.hw2_sopt.data.ResponseLogin
import com.example.hw2_sopt.data.ResponseRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestInterface{
    @POST("/user/signin")
    fun requestLogin(@Body body : RequestLogin) : Call<ResponseLogin>

    @POST("/user/signup")
    fun requestRegister(@Body body: RequestRegister) : Call<ResponseRegister>
}