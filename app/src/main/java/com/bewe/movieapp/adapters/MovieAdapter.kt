package com.bewe.movieapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bewe.movieapp.data.model.Result
import com.bewe.movieapp.databinding.ItemRowMoviesBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var movieList: ArrayList<Result> = arrayListOf<Result>()

    fun setMovieList(movieList: List<Result>){
        this.movieList = movieList as ArrayList<Result>
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRowMoviesBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedMovie = movieList[position]
                    onItemClickCallback.onItemClicked(clickedMovie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        return ViewHolder(ItemRowMoviesBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        val currentMovie = movieList[position]
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w500${currentMovie.poster_path}")
            .centerCrop()
            .into(holder.binding.ivMovieThumbnail)



        holder.binding.tvMovieName.text = currentMovie.title
        holder.binding.tvMovieReleaseDate.text = formatDate(currentMovie.release_date)
    }

    override fun getItemCount() = movieList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Result)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun formatDate(date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val inputDate = inputFormat.parse(date)
        val outputFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return inputDate?.let { outputFormat.format(it) }
    }
}