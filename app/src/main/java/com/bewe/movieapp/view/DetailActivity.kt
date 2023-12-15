package com.bewe.movieapp.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bewe.movieapp.adapters.MovieAdapter
import com.bewe.movieapp.data.model.GenreX
import com.bewe.movieapp.data.model.Result
import com.bewe.movieapp.databinding.ActivityDetailBinding
import com.bewe.movieapp.viewmodel.MovieViewModel
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: MovieViewModel
    private var genreList: List<GenreX> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActionBar()

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        // Observe the genre data
        viewModel.observeGenreLiveData().observe(this) { genres ->
            genreList = genres
            // Call the method to show movie data after fetching the genre list
            showData()
        }

        // Fetch the genre data
        viewModel.getGenres()

        binding.btnBackToMain.setOnClickListener{
            finish()
        }
    }

    private fun showData(){
        val movieDetail =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("MOVIE_DETAIL", Result::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra<Result>("MOVIE_DETAIL")
            }

        if (movieDetail != null) {
            val movieRating = movieDetail.vote_average ?: 0.0
            val formattedMovieRating = String.format("%.1f", movieRating).toDouble()


            // Map genre IDs to genre names
            val genreNames = movieDetail.genre_ids.mapNotNull { id ->
                genreList.find { it.id == id }?.name
            } ?: emptyList()

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movieDetail.poster_path}")
                .into(binding.ivMovieDetailThumbnail)
            binding.tvMovieDetailTitle.text = movieDetail.title
            binding.tvMovieDetailGenre.text = genreNames.joinToString(", ")
            binding.tvMovieDetailReleaseDate.text = MovieAdapter().formatDate(movieDetail.release_date)
            binding.tvMovieDetailOverview.text = movieDetail.overview
            binding.tvMovieDetailRating.text = formattedMovieRating.toString()
        } else {
            Toast.makeText(this, "Error: Movie Detail not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initActionBar(){
        supportActionBar?.apply {
            title = "" // Hide the title
            setDisplayHomeAsUpEnabled(true) // Show the back button
        }
    }
}