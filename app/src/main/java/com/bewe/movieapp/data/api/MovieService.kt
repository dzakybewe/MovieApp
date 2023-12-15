package com.bewe.movieapp.data.api

import com.bewe.movieapp.data.model.Genre
import com.bewe.movieapp.data.model.Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular?")
    fun getPopularMovies(
        @Query("api_key")
        api_key: String
    ) : Call<Movies>

    @GET("genre/movie/list?language=en&")
    fun getGenre(
        @Query("api_key")
        api_key: String
    ) : Call<Genre>
}