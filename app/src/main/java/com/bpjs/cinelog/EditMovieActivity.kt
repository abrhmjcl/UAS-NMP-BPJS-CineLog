package com.bpjs.cinelog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bpjs.cinelog.databinding.ActivityNewMovieBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class EditMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMovieBinding
    private var movieIndex: Int = -1

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra("movie_id", -1)
        movieIndex = MovieData.movies.indexOfFirst { it.id == movieId }
        if (movieIndex < 0 || movieIndex >= MovieData.movies.size) {
            finish()
            return
        }

        binding.lblFormHeader.text = "Edit Detail Film"
        binding.btnSubmitMovie.text = "Simpan Perubahan"

        setupSpinners()
        prepopulateFields()
        setupPosterUrlEnter()
        setupCamera()
        setupSaveButton()
    }

    private fun setupSpinners() {
        binding.spinnerGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, MovieData.genres)
        binding.spinnerPlatform.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, MovieData.platforms)
    }

    private fun prepopulateFields() {
        val movie = MovieData.movies[movieIndex]
        with(binding) {
            txtInputTitle.setText(movie.title)
            txtInputUrl.setText(movie.url)
            txtInputTrailer.setText(movie.trailerUrl)
            txtInputRating.setText(movie.rating.toString())
            txtInputReview.setText(movie.review)

            val genrePos = MovieData.genres.indexOf(movie.genre)
            if (genrePos >= 0) spinnerGenre.setSelection(genrePos)

            val platPos = MovieData.platforms.indexOf(movie.platform)
            if (platPos >= 0) spinnerPlatform.setSelection(platPos)

            when (movie.ageRating) {
                "13+" -> rb13.isChecked = true
                "17+" -> rb17.isChecked = true
                else -> rbSU.isChecked = true
            }

            cbIsWatched.isChecked = movie.isAvailable
            cbHasSpoilers.isChecked = movie.hasSpoilers
            cbIsFavorite.isChecked = movie.isFavorite

            if (movie.url.isNotEmpty()) {
                Picasso.Builder(this@EditMovieActivity).build().load(movie.url).into(imgFormPreview)
            }
        }
    }

    private fun setupPosterUrlEnter() {
        binding.txtInputUrl.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val url = binding.txtInputUrl.text.toString().trim()
                if (url.isNotEmpty()) {
                    Picasso.Builder(this).build().load(url).into(binding.imgFormPreview)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupCamera() {
        binding.btnCapturePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            } else {
                takePicture()
            }
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            } else {
                Toast.makeText(this, "You must grant permission to access the camera.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                binding.imgFormPreview.setImageBitmap(imageBitmap)
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSubmitMovie.setOnClickListener {
            val movie = MovieData.movies[movieIndex]
            val title = binding.txtInputTitle.text.toString().trim()
            val genre = binding.spinnerGenre.selectedItem.toString()
            val platform = binding.spinnerPlatform.selectedItem.toString()
            val ageRating = when (binding.rgAgeRating.checkedRadioButtonId) {
                R.id.rb13 -> "13+"
                R.id.rb17 -> "17+"
                else -> "SU"
            }
            val url = binding.txtInputUrl.text.toString().trim()
            val trailer = binding.txtInputTrailer.text.toString().trim().ifEmpty { "https://www.youtube.com" }
            val rating = binding.txtInputRating.text.toString().toDoubleOrNull() ?: movie.rating
            val review = binding.txtInputReview.text.toString().trim()
            val isAvailable = binding.cbIsWatched.isChecked
            val hasSpoilers = binding.cbHasSpoilers.isChecked
            val isFavorite = binding.cbIsFavorite.isChecked

            if (title.isEmpty()) {
                binding.layoutInputTitle.error = "Judul film tidak boleh kosong"
                return@setOnClickListener
            } else {
                binding.layoutInputTitle.error = null
            }

            val q = Volley.newRequestQueue(this)
            val editUrl = "http://10.0.2.2/cinelog/editmovie.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                editUrl,
                { response ->
                    Log.d("editmovie", response)
                    try {
                        val obj = JSONObject(response)
                        if (obj.getString("result") == "SUCCESS") {
                            movie.title = title
                            movie.genre = genre
                            movie.platform = platform
                            movie.ageRating = ageRating
                            movie.url = url
                            movie.trailerUrl = trailer
                            movie.rating = rating
                            movie.review = review
                            movie.isAvailable = isAvailable
                            movie.hasSpoilers = hasSpoilers
                            movie.isFavorite = isFavorite
                            Toast.makeText(this, "Perubahan berhasil disimpan", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val msg = obj.optString("message", "Gagal menyimpan perubahan")
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error parsing server response", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.e("editmovie", error.message.toString())
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = movie.id.toString()
                    params["title"] = title
                    params["genre"] = genre
                    params["platform"] = platform
                    params["age_rating"] = ageRating
                    params["release_year"] = movie.releaseYear.toString()
                    params["rating"] = rating.toString()
                    params["url"] = url
                    params["trailer_url"] = trailer
                    params["review"] = review
                    params["is_available"] = if (isAvailable) "1" else "0"
                    params["has_spoilers"] = if (hasSpoilers) "1" else "0"
                    params["is_favorite"] = if (isFavorite) "1" else "0"
                    return params
                }
            }

            q.add(stringRequest)
        }
    }
}