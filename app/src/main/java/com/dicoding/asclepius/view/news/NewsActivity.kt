package com.dicoding.asclepius.view.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.view.history.HistoryActivity
import com.dicoding.asclepius.view.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

    // Deklarasi variabel untuk binding, adapter, viewModel, dan elemen lainnya
    private lateinit var binding: ActivityNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menginisialisasi binding dan set layout activity
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur Bottom Navigation Menu dan Listener
        setupBottomNavigation()

        // Menginisialisasi RecyclerView untuk menampilkan berita
        initRecyclerView()

        // Menginisialisasi ViewModel untuk mengambil data berita
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // Mengambil data berita dan mengamati perubahan data
        observeNewsData()

        // Mengamati perubahan status loading dan menampilkan progress bar
        observeLoadingStatus()
    }

    /**
     * Mengatur bottom navigation untuk berpindah antar halaman
     */
    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.menuBar)
        bottomNavigationView.selectedItemId = R.id.news_menu

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_menu -> {
                    // Arahkan ke MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.news_menu -> {
                    // Tetap di NewsActivity
                    true
                }
                R.id.history_menu -> {
                    // Arahkan ke HistoryActivity
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Menginisialisasi RecyclerView untuk menampilkan daftar berita
     */
    private fun initRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvNewsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }

    /**
     * Mengamati perubahan data berita dan mengupdate adapter RecyclerView
     */
    private fun observeNewsData() {
        newsViewModel.fetchHealthNews()
        newsViewModel.newsList.observe(this, Observer { newsList ->
            // Update adapter dengan daftar berita yang diterima
            newsAdapter.submitList(newsList)
        })
    }

    /**
     * Mengamati status loading dan menampilkan progress bar sesuai dengan status
     */
    private fun observeLoadingStatus() {
        newsViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    /**
     * Menampilkan atau menyembunyikan progress bar tergantung pada status loading
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Menghandle pemilihan item di menu options
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Kembali ke aktivitas sebelumnya
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Membuka URL berita ketika pengguna mengklik berita
     */
    fun openNewsUrl(view: View) {
        val url = view.getTag(R.id.tvLink) as? String
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}