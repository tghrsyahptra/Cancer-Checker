package com.dicoding.asclepius.view.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databases.AppDatabase
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.util.ViewModelFactory
import com.dicoding.asclepius.view.main.MainActivity
import com.dicoding.asclepius.view.news.NewsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * HistoryActivity menampilkan riwayat prediksi dan menyediakan navigasi antara menu
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var textNotFound: TextView
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    /**
     * Fungsi ini diinisialisasi saat Activity pertama kali dibuat.
     * Menyiapkan tampilan dan melakukan pengaturan navigasi.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menyiapkan BottomNavigationView dan pengaturan item menu
        initializeNavigation()

        // Mengatur tampilan RecyclerView dan observer data
        initializeRecyclerView()

        // Mengamati perubahan data dari ViewModel
        observeLiveData()

        // Menyembunyikan action bar
        supportActionBar?.hide()
    }

    /**
     * Fungsi untuk mengatur navigasi BottomNavigationView antara Home, News, dan History.
     */
    private fun initializeNavigation() {
        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.history_menu
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_menu -> navigateToMainActivity()
                R.id.news_menu -> navigateToNewsActivity()
                R.id.history_menu -> true
                else -> false
            }
        }
    }

    /**
     * Fungsi untuk menavigasi ke MainActivity.
     */
    private fun navigateToMainActivity(): Boolean {
        startActivity(Intent(this, MainActivity::class.java))
        return true
    }

    /**
     * Fungsi untuk menavigasi ke NewsActivity.
     */
    private fun navigateToNewsActivity(): Boolean {
        startActivity(Intent(this, NewsActivity::class.java))
        return true
    }

    /**
     * Fungsi untuk mengamati hasil data yang diterima dari ViewModel dan memperbarui tampilan.
     */
    private fun observeLiveData() {
        viewModel.results.observe(this) { setResults(it) }
    }

    /**
     * Fungsi untuk mengatur tampilan hasil prediksi pada RecyclerView.
     */
    private fun setResults(results: List<ClassificationResult>) {
        val adapter = ResultAdapter()
        adapter.submitList(results)
        binding.rvHistory.adapter = adapter
    }

    /**
     * Fungsi untuk menyiapkan RecyclerView dengan LayoutManager dan item decoration.
     */
    private fun initializeRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }
}