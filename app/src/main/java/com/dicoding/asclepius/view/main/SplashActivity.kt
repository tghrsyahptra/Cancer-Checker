package com.dicoding.asclepius.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R

/**
 * Aktivitas SplashScreen yang menampilkan layar pembuka dengan animasi
 * sebelum beralih ke MainActivity.
 */
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DELAY: Long = 3000 // Durasi delay sebelum beralih ke MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Inisialisasi ImageView untuk gambar splash dan mulai animasi
        val splashImage = findViewById<ImageView>(R.id.splashImage)
        startSplashAnimation(splashImage)

        // Beralih ke MainActivity setelah delay yang ditentukan
        navigateToMainActivityWithDelay()
    }

    /**
     * Memulai animasi fade-in pada gambar splash.
     * @param splashImage ImageView yang menampilkan gambar splash
     */
    private fun startSplashAnimation(splashImage: ImageView) {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        splashImage.startAnimation(fadeInAnimation)
    }

    /**
     * Beralih ke MainActivity setelah delay yang telah ditentukan.
     * Fungsi ini menggunakan Handler untuk menunda pemindahan aktivitas.
     */
    private fun navigateToMainActivityWithDelay() {
        // Menunda peralihan ke MainActivity selama SPLASH_DELAY
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Menutup SplashActivity agar tidak kembali saat menekan tombol kembali
        }, SPLASH_DELAY)
    }
}