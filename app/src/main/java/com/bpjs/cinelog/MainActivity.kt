package com.bpjs.cinelog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bpjs.cinelog.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Siapkan daftar fragment untuk 3 Tab
        val fragments: ArrayList<Fragment> = ArrayList()
        fragments.add(MovieListFragment.newInstance(MovieListFragment.MODE_NOW_PLAYING))
        fragments.add(MovieListFragment.newInstance(MovieListFragment.MODE_PLAN_TO_WATCH))
        fragments.add(FavoriteFragment())

        // 2. Pasang Adapter ke ViewPager2
        binding.viewPager.adapter = MovieViewPagerAdapter(this, fragments)

        // 3. Sinkronisasi manual ViewPager2 ke TabLayout (Sesuai Materi Week 9)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        // 4. Sinkronisasi manual TabLayout ke ViewPager2
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // 5. FAB Tambah Film
        binding.fabAddMovie.setOnClickListener {
            // Nanti dihubungkan ke form tambah film oleh Person 1
        }
    }

    // Fungsi bantuan hapus film (akan diintegrasikan ke API Volley oleh Person 3)
    fun deleteMovieFromRemote(movie: MovieItem) {
        val index = MovieData.movies.indexOfFirst { it.id == movie.id }
        if (index != -1) {
            MovieData.movies.removeAt(index)
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
    }
}