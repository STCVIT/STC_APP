package com.mstc.mstcapp.util

import com.mstc.mstcapp.model.Feed
import com.mstc.mstcapp.model.ProjectIdea
import com.mstc.mstcapp.model.explore.BoardMember
import com.mstc.mstcapp.model.explore.Event
import com.mstc.mstcapp.model.explore.Project
import com.mstc.mstcapp.model.resource.Detail
import com.mstc.mstcapp.model.resource.Resource
import com.mstc.mstcapp.model.resource.Roadmap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {

    @GET("project")
    suspend fun getProjects(): Response<List<Project>>

    @GET("event")
    suspend fun getEvents(): Response<List<Event>>

    @GET("feed")
    suspend fun getFeed(@Query("skip") skip: Int): List<Feed>

    @GET("board")
    suspend fun getBoard(): Response<List<BoardMember>>

    @GET("details/{domain}")
    suspend fun getDetails(@Path("domain") domain: String): Response<Detail>

    @GET("roadmap/{domain}")
    suspend fun getRoadmap(@Path("domain") domain: String): Response<Roadmap>

    @GET("resource/{domain}")
    suspend fun getResources(@Path("domain") domain: String): Response<List<Resource>>

    @POST("idea")
    suspend fun postIdea(@Body projectIdeaModel: ProjectIdea): Response<Map<String, String>>

    companion object {
        private const val BASE_URL = "https://stcbackend.herokuapp.com/"
        fun create(): RetrofitService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}