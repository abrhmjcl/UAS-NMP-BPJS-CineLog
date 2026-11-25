package com.bpjs.cinelog

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bpjs.cinelog.databinding.MovieCardBinding
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movies: ArrayList<MovieItem>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(val binding: MovieCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    private fun openDetailDialog(movie: MovieItem) {
        val dialog = MovieDetailFragment.newInstance(movie)
        dialog.show(fragmentManager, "MovieDetail")
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        val context = holder.itemView.context

        with(holder.binding) {
            txtMovieTitle.text = movie.title
            txtGenrePlatform.text = "${movie.genre} - ${movie.platform} (${movie.ageRating})"
            txtRatingScore.text = "Rating: ${movie.rating}/10"

            if (movie.isAvailable) {
                txtWatchStatusBadge.text = "WATCHED"
                txtWatchStatusBadge.setBackgroundResource(R.drawable.bg_badge_watched)
                txtWatchStatusBadge.setTextColor(context.getColor(R.color.white))
            } else {
                txtWatchStatusBadge.text = "PLAN TO WATCH"
                txtWatchStatusBadge.setBackgroundResource(R.drawable.bg_badge_plan)
                txtWatchStatusBadge.setTextColor(context.getColor(R.color.white))
            }

            if (movie.url.isNotEmpty()) {
                val builder = Picasso.Builder(context)
                builder.listener { _, _, exception -> exception.printStackTrace() }
                builder.build().load(movie.url).into(imgPoster)
            } else if (movie.imageId != 0) {
                imgPoster.setImageResource(movie.imageId)
            } else {
                imgPoster.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            if (!movie.isAvailable) {
                val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
                imgPoster.colorFilter = ColorMatrixColorFilter(colorMatrix)
            } else {
                imgPoster.colorFilter = null
            }

            btnCardEdit.setOnClickListener {
                val intent = Intent(context, EditMovieActivity::class.java)
                intent.putExtra("movie_id", movie.id)
                intent.putExtra("movie_item", movie)
                context.startActivity(intent)
            }

            btnCardDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle(R.string.dialog_delete_title)
                    .setMessage("Hapus '${movie.title}' dari koleksi?")
                    .setPositiveButton("Hapus") { dialog, _ ->
                        if (context is MainActivity) {
                            context.deleteMovieFromRemote(movie)
                        } else {
                            val currentPos = holder.bindingAdapterPosition
                            if (currentPos != RecyclerView.NO_POSITION) {
                                movies.removeAt(currentPos)
                                notifyItemRemoved(currentPos)
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            root.setOnClickListener {
                if (movie.hasSpoilers) {
                    AlertDialog.Builder(context)
                        .setTitle(R.string.dialog_spoiler_title)
                        .setMessage(R.string.dialog_spoiler_message)
                        .setPositiveButton("Buka Review") { d, _ ->
                            d.dismiss()
                            openDetailDialog(movie)
                        }
                        .setNegativeButton("Batal") { d, _ -> d.dismiss() }
                        .create()
                        .show()
                } else {
                    openDetailDialog(movie)
                }
            }
        }
    }
}