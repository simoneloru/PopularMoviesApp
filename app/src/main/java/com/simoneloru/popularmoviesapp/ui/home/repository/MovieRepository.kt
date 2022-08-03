package com.simoneloru.popularmoviesapp.ui.home.repository

import com.simoneloru.popularmoviesapp.data.MovieService
import com.simoneloru.popularmoviesapp.data.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(
    private val movieAppService: MovieService
) {

    suspend fun fetchPopular(apikey: String, page: Int? = null): Response<MovieResponse> = withContext(
        Dispatchers.IO) {
        val popular = movieAppService.getPopularMovies(apikey, page)
        popular
    }
}