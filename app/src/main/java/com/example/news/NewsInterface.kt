package com.example.news

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://newsapi.org/"
const val APIKey = "78ce5a32068c435faa5963affc586149"

interface NewsInterface {

    @GET("v2/top-headlines?apiKey=${APIKey}")
    suspend fun getHeadlines(@Query("country") country : String, @Query("page") page: Int) : Response<News>

}

