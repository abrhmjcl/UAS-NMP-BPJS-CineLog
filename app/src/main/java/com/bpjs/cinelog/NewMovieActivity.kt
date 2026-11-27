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
import androidx.appcompat.app.AppCompatActivityS
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bpjs.cinelog.databinding.ActivityNewMovieBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class NewMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMovieBinding

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupPosterUrlEnter()
        setupCamera()
        setupSubmitButton()
    }

    private fun setupSpinners() {
        val genreAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, MovieData.genres)
        binding.spinnerGenre.adapter = genreAdapter

        val platformAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, MovieData.platforms)
        binding.spinnerPlatform.adapter = platformAdapter
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

    private fun setupSubmitButton() {
        binding.btnSubmitMovie.setOnClickListener {
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
            val rating = binding.txtInputRating.text.toString().toDoubleOrNull() ?: 0.0
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
            val postUrl = "http://10.0.2.2/cinelog/insertmovie.php"
            val stringRequest = object : StringRequest(
                Request.Method.POST,
                postUrl,
                { response ->
                    Log.d("insertmovie", response)
                    try {
                        val obj = JSONObject(response)
                        if (obj.getString("result") == "SUCCESS") {
                            val generatedId = obj.optInt("id", MovieData.movies.size + 1)
                            val newMovie = MovieItem(
                                id = generatedId,
                                title = title,
                                genre = genre,
                                platform = platform,
                                ageRating = ageRating,
                                releaseYear = 2024,
                                rating = rating,
                                url = url,
                                trailerUrl = trailer,
                                review = review,
                                isAvailable = isAvailable,
                                hasSpoilers = hasSpoilers,
                                isFavorite = isFavorite
                            )
                            MovieData.movies.add(0, newMovie)
                            Toast.makeText(this, "Film berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val msg = obj.optString("message", "Gagal menambahkan film")
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error parsing server response", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    Log.e("insertmovie", error.message.toString())
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["title"] = title
                    params["genre"] = genre
                    params["platform"] = platform
                    params["age_rating"] = ageRating
                    params["release_year"] = "2024"
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