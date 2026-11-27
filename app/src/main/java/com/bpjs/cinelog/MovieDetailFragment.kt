package com.bpjs.cinelog

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.bpjs.cinelog.databinding.FragmentMovieDetailBinding
import com.squareup.picasso.Picasso

class MovieDetailFragment : DialogFragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private var movie: MovieItem? = null

    companion object {
        private const val ARG_MOVIE = "movie_key"

        @JvmStatic
        fun newInstance(movie: MovieItem) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MOVIE, movie)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = BundleCompat.getParcelable(it, ARG_MOVIE, MovieItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        movie?.let { m ->
            with(binding) {
                txtDetailTitle.text = m.title
                txtDetailMeta.text = "${m.genre} - ${m.platform} - Rating: ${m.rating}/10"
                txtDetailReview.text = "\"${m.review}\""

                if (m.url.isNotEmpty()) {
                    Picasso.Builder(requireContext()).build().load(m.url).into(imgDetailPoster)
                }

                btnWatchTrailer.setOnClickListener {
                    val intent = Intent(requireContext(), CinemaWebActivity::class.java)
                    intent.putExtra("extra_url", m.trailerUrl)
                    startActivity(intent)
                }

                btnShareReview.setOnClickListener {
                    val shareText = "CineLog Review: ${m.title} (${m.platform})\nRating: ${m.rating}/10\n\"${m.review}\""
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                    sendIntent.type = "text/plain"
                    val shareIntent = Intent.createChooser(sendIntent, "Bagikan Review")
                    startActivity(shareIntent)
                }

                btnCinemaMaps.setOnClickListener {
                    val mapUri = Uri.parse("geo:0,0?q=Cinema+XXI+Surabaya")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                    startActivity(mapIntent)
                }

                btnCinemaDial.setOnClickListener {
                    val dialUri = Uri.parse("tel:0311234567")
                    val dialIntent = Intent(Intent.ACTION_DIAL, dialUri)
                    startActivity(dialIntent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}