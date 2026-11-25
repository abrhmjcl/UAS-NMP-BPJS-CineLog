package com.bpjs.cinelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bpjs.cinelog.databinding.FragmentMovieListBinding

class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private var mode: Int = MODE_NOW_PLAYING

    companion object {
        const val MODE_NOW_PLAYING = 0
        const val MODE_PLAN_TO_WATCH = 1
        private const val ARG_MODE = "arg_mode"

        @JvmStatic
        fun newInstance(mode: Int) = MovieListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = arguments?.getInt(ARG_MODE) ?: MODE_NOW_PLAYING
    }

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
        refreshFilteredData()
    }

    fun refreshFilteredData() {
        if (_binding == null) return
        val filtered = when (mode) {
            MODE_PLAN_TO_WATCH -> MovieData.movies.filter { !it.isAvailable }
            else -> MovieData.movies.filter { it.isAvailable }
        }
        binding.recyclerMovies.adapter = MovieAdapter(ArrayList(filtered), parentFragmentManager)
    }

    override fun onResume() {
        super.onResume()
        refreshFilteredData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}