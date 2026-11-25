package com.bpjs.cinelog

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bpjs.cinelog.databinding.ActivityIntroBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding

    companion object {
        const val KEY_USERNAME = "username_key"
        const val KEY_BOD = "bod_key"
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                binding.txtBod.setText(format.format(selectedDate.time))
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedFile = packageName
        val shared: SharedPreferences = getSharedPreferences(sharedFile, Context.MODE_PRIVATE)

        val savedUser = shared.getString(KEY_USERNAME, "")
        val savedBod = shared.getString(KEY_BOD, "")

        if (!savedUser.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(KEY_USERNAME, savedUser)
                putExtra(KEY_BOD, savedBod)
            }
            startActivity(intent)
            finish()
            return
        }

        binding.txtBod.setOnClickListener { showDatePickerDialog() }

        binding.btnMasuk.setOnClickListener {
            val username = binding.txtUsername.text.toString().trim()
            val bod = binding.txtBod.text.toString().trim()

            if (username.isEmpty()) {
                binding.textInputLayoutUsername.error = "Nama wajib diisi"
                return@setOnClickListener
            } else {
                binding.textInputLayoutUsername.error = null
            }

            if (bod.isEmpty()) {
                binding.textInputLayoutBod.error = "Tanggal lahir wajib diisi"
                return@setOnClickListener
            } else {
                binding.textInputLayoutBod.error = null
            }

            val editor = shared.edit()
            editor.putString(KEY_USERNAME, username)
            editor.putString(KEY_BOD, bod)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(KEY_USERNAME, username)
                putExtra(KEY_BOD, bod)
            }
            startActivity(intent)
            finish()
        }
    }
}