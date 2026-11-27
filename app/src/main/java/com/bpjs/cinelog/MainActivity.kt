package com.bpjs.cinelog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bpjs.cinelog.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var nowPlayingFragment: MovieListFragment
    private lateinit var planToWatchFragment: MovieListFragment
    private lateinit var favoriteFragment: FavoriteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nowPlayingFragment = MovieListFragment.newInstance(MovieListFragment.MODE_NOW_PLAYING)
        planToWatchFragment = MovieListFragment.newInstance(MovieListFragment.MODE_PLAN_TO_WATCH)
        favoriteFragment = FavoriteFragment()

        val fragments: ArrayList<Fragment> = ArrayList()
        fragments.add(nowPlayingFragment)
        fragments.add(planToWatchFragment)
        fragments.add(favoriteFragment)

        binding.viewPager.adapter = MovieViewPagerAdapter(this, fragments)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.fabAddMovie.setOnClickListener {
            val intent = Intent(this, NewMovieActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMoviesFromRemote()
    }

    private fun refreshAllTabs() {
        nowPlayingFragment.refreshFilteredData()
        planToWatchFragment.refreshFilteredData()
        favoriteFragment.refreshFavorites()
    }

    fun fetchMoviesFromRemote() {
        val q = Volley.newRequestQueue(this)
        val url = "http://10.0.2.2/cinelog/getmovies.php"
        val stringRequest = StringRequest(
            Request.Method.POST,
            url,
            { response ->
                Log.d("apiresult", response)
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "SUCCESS") {
                        val array = obj.getJSONArray("data")
                        val remoteList = ArrayList<MovieItem>()
                        for (i in 0 until array.length()) {
                            val item = array.getJSONObject(i)
                            remoteList.add(
                                MovieItem(
                                    id = item.getInt("id"),
                                    title = item.getString("title"),
                                    genre = item.getString("genre"),
                                    platform = item.getString("platform"),
                                    ageRating = item.getString("age_rating"),
                                    releaseYear = item.getInt("release_year"),
                                    rating = item.getDouble("rating"),
                                    url = item.getString("url"),
                                    trailerUrl = item.getString("trailer_url"),
                                    review = item.getString("review"),
                                    isAvailable = item.getInt("is_available") == 1,
                                    hasSpoilers = item.getInt("has_spoilers") == 1,
                                    isFavorite = item.getInt("is_favorite") == 1
                                )
                            )
                        }
                        MovieData.movies = remoteList
                        refreshAllTabs()
                    }
                } catch (e: Exception) {
                    Log.e("apiresult", e.message.toString())
                }
            },
            { error ->
                Log.e("apiresult", error.message.toString())
                Toast.makeText(this, "Server lokal offline, memuat data statis", Toast.LENGTH_SHORT).show()
            }
        )

        q.add(stringRequest)
    }

    fun deleteMovieFromRemote(movie: MovieItem) {
        val q = Volley.newRequestQueue(this)
        val deleteUrl = "http://10.0.2.2/cinelog/deletemovie.php"
        val stringRequest = object : StringRequest(
            Request.Method.POST,
            deleteUrl,
            { response ->
                Log.d("deletemovie", response)
                try {
                    val obj = JSONObject(response)
                    if (obj.getString("result") == "SUCCESS") {
                        MovieData.movies.remove(movie)
                        refreshAllTabs()
                        Toast.makeText(this, "${movie.title} dihapus", Toast.LENGTH_SHORT).show()
                    } else {
                        val msg = obj.optString("message", "Gagal menghapus film")
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing server response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("deletemovie", error.message.toString())
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = movie.id.toString()
                return params
            }
        }
        q.add(stringRequest)
    }
}