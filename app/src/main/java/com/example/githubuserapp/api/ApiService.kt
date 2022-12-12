package com.example.githubuserapp.api

import com.example.githubuserapp.BuildConfig
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.data.response.DetailUserResponse
import com.example.githubuserapp.data.response.UserResponse
import com.example.githubuserapp.data.response.RepositoryResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    @Headers("Autohorization: token $apiKey")
    fun getAllUser(): Call<ArrayList<ItemsItem>>

    @GET("search/users")
    @Headers("Autohorization: token $apiKey")
    fun getUserList(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{login}")
    @Headers("Autohorization: token $apiKey")
    fun getDetailUser(
        @Path("login") login: String
    ): Call<DetailUserResponse>

    @GET("users/{login}/repos")
    @Headers("Autohorization: token $apiKey")
    fun getRepositoryUser(
        @Path("login") login: String
    ): Call<ArrayList<RepositoryResponseItem>>

    @GET("users/{login}/followers")
    @Headers("Autohorization: token $apiKey")
    fun getDetailFollowerUser(
        @Path("login") login: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{login}/following")
    @Headers("Autohorization: token $apiKey")
    fun getDetailFollowingUser(
        @Path("login") login: String
    ): Call<ArrayList<ItemsItem>>

    companion object {
        private const val apiKey: String = BuildConfig.APP_KEY
    }
}