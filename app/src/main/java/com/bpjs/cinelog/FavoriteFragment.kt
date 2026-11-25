package com.bpjs.cinelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bpjs.cinelog.databinding.FragmentMovieListBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.setHasFixedSize(true)
        refreshFavorites()
    }

    fun refreshFavorites() {
        if (_binding == null) return
        val favorites = MovieData.movies.filter { it.isFavorite }
        binding.recyclerMovies.adapter = MovieAdapter(ArrayList(favorites), parentFragmentManager)
    }

    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}