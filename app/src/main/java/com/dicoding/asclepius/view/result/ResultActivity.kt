package com.dicoding.asclepius.view.result

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.util.ViewModelFactory
import com.dicoding.asclepius.view.main.MainActivity
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

/**
 * Activity untuk menampilkan hasil klasifikasi gambar.
 * Menyediakan fitur untuk menampilkan hasil klasifikasi dan menyimpan atau menghapus hasil tersebut.
 */
class ResultActivity : AppCompatActivity() {

    // Binding untuk mengakses elemen tampilan
    private lateinit var binding: ActivityResultBinding

    // Helper untuk mengklasifikasikan gambar
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    // ViewModel untuk mengelola data hasil klasifikasi
    private val viewModel: ResultViewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // Menyimpan hasil klasifikasi
    private var result: ClassificationResult? = null

    /**
     * Menginisialisasi activity dan mempersiapkan elemen-elemen yang diperlukan.
     * Memeriksa apakah ada gambar yang dipilih atau hasil klasifikasi yang diberikan.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menyembunyikan action bar
        supportActionBar?.hide()

        // Memeriksa apakah ada URI gambar yang diteruskan melalui intent
        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
            imageUri?.let {
                binding.resultImage.setImageURI(it)
            }
            // Menyiapkan image classifier dan memulai klasifikasi gambar
            setupImageClassifier(imageUri)
            imageClassifierHelper.classifyStaticImage(imageUri)

            // Menyimpan hasil klasifikasi
            binding.btnSave.setOnClickListener {
                result?.let { viewModel.insert(it) }
                finish()
            }

            // Kembali ke activity sebelumnya
            binding.btnBack.setOnClickListener {
                goBack()
            }

            // Memeriksa apakah ada hasil klasifikasi yang diteruskan melalui intent
        } else if (intent.hasExtra(EXTRA_RESULT)) {
            result = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_RESULT, ClassificationResult::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_RESULT)
            }

            // Menampilkan hasil klasifikasi yang diterima
            result?.let {
                updateView(Uri.parse(it.imageUri), it.label, it.score)
                binding.btnSave.setOnClickListener {
                    // Menghapus hasil klasifikasi jika ditekan tombol save
                    viewModel.delete(result!!)
                    finish()
                }
            }
            // Mengubah teks tombol menjadi "Delete"
            binding.btnSave.text = getString(R.string.delete)
        }
    }

    /**
     * Menyiapkan image classifier untuk mengklasifikasikan gambar.
     * Menginisialisasi ImageClassifierHelper dengan listener untuk menangani hasil klasifikasi.
     */
    private fun setupImageClassifier(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                // Menangani error klasifikasi
                override fun onError(error: String) {
                    Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                }

                // Menangani hasil klasifikasi
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { classifications ->
                            if (classifications.isNotEmpty() && classifications[0].categories.isNotEmpty()) {
                                println(classifications)
                                classifications[0].categories.maxByOrNull { it.score }!!.let {
                                    // Menampilkan hasil klasifikasi pada UI
                                    updateView(imageUri, it.label, it.score)
                                    // Menyimpan hasil klasifikasi untuk digunakan kembali
                                    result = ClassificationResult(
                                        timestamp = System.currentTimeMillis(),
                                        imageUri = imageUri.toString(),
                                        label = it.label,
                                        score = it.score
                                    )
                                }
                            } else {
                                binding.resultText.text = ""
                            }
                        }
                    }
                }
            }
        )
    }

    /**
     * Memperbarui tampilan UI dengan hasil klasifikasi.
     * Menampilkan gambar, label, dan score, serta memperbarui progress bar.
     */
    private fun updateView(imageUri: Uri, label: String, score: Float) {
        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text =
            buildString {
                append(NumberFormat.getPercentInstance().format(score))
                append(" $label")
            }

        // Memperbarui progress bar berdasarkan hasil klasifikasi
        if (label == "Cancer") {
            binding.linearProgressBar.progress = score.times(100).toInt()
        } else {
            binding.linearProgressBar.progress = 100 - score.times(100).toInt()
            binding.resultText.setTextColor(getColor(R.color.red))
        }
    }

    /**
     * Menavigasi kembali ke MainActivity.
     */
    private fun goBack() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        // Konstanta untuk kunci intent
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}