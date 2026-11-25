package com.bpjs.cinelog

import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MovieDetailFragment : DialogFragment() {
    companion object {
        fun newInstance(movie: MovieItem): MovieDetailFragment {
            return MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("movie", movie)
                }
            }
        }
    }
}