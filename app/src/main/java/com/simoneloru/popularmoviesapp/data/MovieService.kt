package com.simoneloru.popularmoviesapp.data

import com.simoneloru.popularmoviesapp.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService {

    companion object {
        const val ENDPOINT = "https://api.themoviedb.org/3/"
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String?, @Query("page") page: Int? = null): Response<MovieResponse>

}