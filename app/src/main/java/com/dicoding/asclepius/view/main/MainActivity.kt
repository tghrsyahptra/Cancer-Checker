package com.dicoding.asclepius.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.result.ResultActivity
import com.dicoding.asclepius.view.news.NewsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val imageViewModel: ImageViewModel by viewModels() // Inisialisasi ViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private var tempSelectedImageUri: Uri? = null // Menyimpan URI gambar yang dipilih dari galeri sementara
    private var isCroppingSuccess = false // Flag untuk mengecek apakah cropping berhasil atau dibatalkan

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_menu -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.news_menu -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    true
                }
                R.id.history_menu -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            imageViewModel.currentImageUri?.let {
                analyzeImage()
                moveToResult()
            } ?: run {
                showToast(getString(R.string.image_classifier_failed))
            }
        }

        // Tampilkan gambar jika ada URI di ViewModel saat orientasi berubah
        imageViewModel.currentImageUri?.let { binding.previewImageView.setImageURI(it) }
        imageViewModel.croppedImageUri?.let { binding.previewImageView.setImageURI(it) }

        updateButtonStatus()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                // Simpan URI gambar yang dipilih sementara (sebelum crop)
                tempSelectedImageUri = uri
                // Tampilkan gambar yang dipilih
                showImage()
                resizeUCrop(uri)
            } ?: showToast("Failed to get image URI")
        }
    }

    private fun resizeUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                // Jika hasil crop berhasil
                val resultUri = UCrop.getOutput(data!!)
                resultUri?.let {
                    showCroppedImage(resultUri)
                    isCroppingSuccess = true // Tandai bahwa cropping berhasil
                } ?: run {
                    showToast("Failed to crop image")
                    isCroppingSuccess = false
                }
            } else if (resultCode == UCrop.RESULT_ERROR) {
                // Jika terjadi kesalahan saat crop
                val cropError = UCrop.getError(data!!)
                showToast("Crop error: ${cropError?.message}")
                isCroppingSuccess = false
            } else if (resultCode == RESULT_CANCELED) {
                // Jika cropping dibatalkan
                showToast("Image crop cancelled")
                restorePreviousImage() // Kembalikan gambar sebelumnya jika cropping dibatalkan
                isCroppingSuccess = false
            }
        }
    }

    private fun showImage() {
        // Tampilkan gambar yang dipilih dari galeri
        tempSelectedImageUri?.let {
            binding.previewImageView.setImageURI(it)
            imageViewModel.currentImageUri = it // Simpan URI gambar
        }
        updateButtonStatus() // Perbarui status tombol
    }

    private fun analyzeImage() {
        val intent = Intent(this, ResultActivity::class.java)
        imageViewModel.croppedImageUri?.let { uri ->
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun moveToResult() {
        Log.d(TAG, "Moving to ResultActivity")
        val intent = Intent(this, ResultActivity::class.java)
        imageViewModel.croppedImageUri?.let { uri ->
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
            startActivity(intent)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun updateButtonStatus() {
        // Button Analyze hanya aktif jika ada gambar yang sudah dipilih atau dipotong
        binding.analyzeButton.isEnabled = imageViewModel.currentImageUri != null || imageViewModel.croppedImageUri != null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showCroppedImage(uri: Uri) {
        binding.previewImageView.setImageURI(uri)
        imageViewModel.croppedImageUri = uri // Simpan URI gambar hasil crop di ViewModel
        // Update URI utama hanya jika cropping berhasil
        imageViewModel.currentImageUri = uri
        updateButtonStatus() // Perbarui status tombol setelah crop
    }


    // Fungsi untuk mengembalikan gambar sebelumnya jika crop dibatalkan
    @SuppressLint("SetTextI18n")
    private fun restorePreviousImage() {
        // Jika gambar sebelumnya ada, tampilkan kembali gambar tersebut
        tempSelectedImageUri?.let {
            binding.previewImageView.setImageURI(it)
            imageViewModel.currentImageUri = it // Kembalikan URI gambar sebelumnya
        }

        // Menampilkan Toast di tengah layar dengan instruksi untuk input gambar ulang
        val toast = Toast.makeText(this, "Input gambar ulang", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0) // Mengatur posisi toast di tengah layar
        toast.show()

        // Sembunyikan tombol "Analyze" dan ganti dengan instruksi untuk input gambar ulang
        binding.analyzeButton.isEnabled = false
        binding.galleryButton.isEnabled = true
        binding.galleryButton.text = "Input Gambar Ulang"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navbar, menu)
        return true
    }
}