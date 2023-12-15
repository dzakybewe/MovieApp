package com.bewe.movieapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bewe.movieapp.data.api.ApiClient
import com.bewe.movieapp.data.model.Genre
import com.bewe.movieapp.data.model.GenreX
import com.bewe.movieapp.data.model.Movies
import com.bewe.movieapp.data.model.Result
import com.bewe.movieapp.util.Constants.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MovieViewModel: ViewModel() {
    private var movieLiveData: MutableLiveData<List<Result>> = MutableLiveData()
    private var genreLiveData: MutableLiveData<List<GenreX>> = MutableLiveData()

    fun getMovies() {
        ApiClient.movieService.getPopularMovies(API_KEY).enqueue(
            object : Callback<Movies>{
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.isSuccessful && response.body()!=null) {
                        movieLiveData.value = response.body()!!.results
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    Log.d("TAG",t.message.toString())
                }

            }
        )
    }
    fun observeMovieLiveData() : LiveData<List<Result>>{
        return movieLiveData
    }

    fun getGenres(){
        ApiClient.movieService.getGenre(API_KEY).enqueue(
            object : Callback<Genre>{
                override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                    if (response.isSuccessful && response.body()!=null) {
                        genreLiveData.value = response.body()!!.genres
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<Genre>, t: Throwable) {
                    Log.d("TAG",t.message.toString())
                }

            }
        )
    }

    fun observeGenreLiveData() : LiveData<List<GenreX>>{
        return genreLiveData
    }
}