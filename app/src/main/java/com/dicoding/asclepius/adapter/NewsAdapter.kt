package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.view.news.NewsItem

/**
 * Adapter untuk menampilkan daftar berita pada RecyclerView
 * Menggunakan ListAdapter untuk pengelolaan data yang efisien dengan DiffUtil
 */
class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    /**
     * Membuat ViewHolder baru saat item di RecyclerView dibuat.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_news, parent, false)
        return NewsViewHolder(itemView)
    }

    /**
     * Mengikat data dari item ke ViewHolder.
     */
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.bind(newsItem)
    }

    /**
     * ViewHolder untuk setiap item berita.
     * Mengikat data ke tampilan dalam item RecyclerView.
     */
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        private val newsImageView: ImageView = itemView.findViewById(R.id.imgNews)

        /**
         * Mengikat data berita ke dalam tampilan item.
         * Menampilkan gambar berita dan teks sesuai dengan data yang ada.
         */
        fun bind(newsItem: NewsItem) {
            titleTextView.text = newsItem.title
            setLinkTextView(newsItem)
            loadImage(newsItem)
        }

        /**
         * Mengatur visibilitas dan URL untuk TextView yang menampilkan "Read more"
         */
        private fun setLinkTextView(newsItem: NewsItem) {
            itemView.findViewById<TextView>(R.id.tvLink).apply {
                text = "Read more"
                setTag(R.id.tvLink, newsItem.url)
                visibility = if (newsItem.url != null) View.VISIBLE else View.GONE
            }
        }

        /**
         * Memuat gambar berita menggunakan Glide.
         * Jika gambar tidak ada, tampilkan placeholder.
         */
        private fun loadImage(newsItem: NewsItem) {
            if (!newsItem.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(newsItem.imageUrl)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(newsImageView)
            } else {
                newsImageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_place_holder))
            }
        }
    }

    /**
     * DiffCallback untuk menentukan apakah item yang sedang dibandingkan adalah item yang sama
     * dan apakah konten item tersebut sama.
     */
    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {

        /**
         * Menentukan apakah dua item adalah item yang sama berdasarkan title berita.
         */
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.title == newItem.title
        }

        /**
         * Menentukan apakah konten dua item sama dengan membandingkan objek secara keseluruhan.
         */
        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}