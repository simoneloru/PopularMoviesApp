package com.simoneloru.popularmoviesapp.ui.home.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simoneloru.popularmoviesapp.data.model.Movie
import com.simoneloru.popularmoviesapp.data.model.MovieResponse
import com.simoneloru.popularmoviesapp.ui.home.repository.MovieRepository
import com.simoneloru.popularmoviesapp.util.ResourceUtil

import com.simoneloru.popularmoviesapp.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val moviePopular: MutableLiveData<ResourceUtil<MovieResponse>> = MutableLiveData()
    var movieListResponse: MovieResponse? = null
    var moviePage = 1

    fun fetchPopular(apikey: String) = viewModelScope.launch {
        safeMovieCall(apikey, moviePage)
    }

    private suspend fun safeMovieCall(apikey: String, page: Int){
        moviePopular.postValue(ResourceUtil.Loading())
        try{
            if(hasInternetConnection(context)){
                val response = movieRepository.fetchPopular(apikey, page)
                moviePopular.postValue(handleOrderResponse(response))
            }
            else{
                moviePopular.postValue(ResourceUtil.Error("No Internet Connection"))
            }
        }
        catch (ex: Exception){
            when(ex){
                is IOException -> moviePopular.postValue(ResourceUtil.Error("Network Failure"))
                else -> moviePopular.postValue(ResourceUtil.Error("Conversion Error"))
            }
        }
    }


    private fun handleOrderResponse(response: Response<MovieResponse>): ResourceUtil<MovieResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                moviePage++
                if (movieListResponse == null)
                    movieListResponse = resultResponse
                else {
                    val oldMovies = movieListResponse!!.movies as ArrayList<Movie>?
                    val newMovies = resultResponse.movies!! as ArrayList<Movie>?
                    oldMovies!!.addAll(newMovies!!)
                }
                return ResourceUtil.Success(movieListResponse ?: resultResponse)
            }
        }
        return ResourceUtil.Error(response.message())
    }

}