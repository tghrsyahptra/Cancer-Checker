package com.dicoding.asclepius.view.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityItemHistoryBinding
import com.dicoding.asclepius.util.parseTimestamp
import com.dicoding.asclepius.view.result.ResultActivity
import java.text.NumberFormat

/**
 * Adapter untuk menampilkan riwayat hasil klasifikasi di RecyclerView.
 */
class ResultAdapter : ListAdapter<ClassificationResult, ResultAdapter.ViewHolder>(DIFF_CALLBACK) {

    /**
     * ViewHolder untuk mengikat data ke setiap item di RecyclerView.
     */
    inner class ViewHolder(private val binding: ActivityItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Mengikat data ClassificationResult ke komponen UI di layout item.
         * @param item ClassificationResult yang akan ditampilkan.
         */
        fun bind(item: ClassificationResult) {
            // Memuat gambar menggunakan Glide ke dalam ImageView
            Glide.with(itemView)
                .load(item.imageUri)
                .into(binding.ivImage)

            // Mengatur teks label
            binding.tvLabel.text = item.label

            // Memformat dan menampilkan skor sebagai persentase
            binding.tvScore.text = NumberFormat.getPercentInstance().format(item.score)

            // Memformat dan menampilkan timestamp
            binding.tvTimestamp.text = parseTimestamp(item.timestamp)

            // Mengubah warna teks berdasarkan nilai label
            if (item.label != "Cancer") {
                binding.tvLabel.setTextColor(itemView.context.getColor(R.color.blue))
                binding.tvScore.setTextColor(itemView.context.getColor(R.color.green))
            }
        }
    }

    companion object {
        /**
         * DiffUtil callback untuk membandingkan item di RecyclerView.
         */
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClassificationResult>() {

            /**
             * Membandingkan keunikan item berdasarkan identifikatornya.
             * @param oldItem ClassificationResult lama.
             * @param newItem ClassificationResult baru.
             * @return True jika item sama, false jika tidak.
             */
            override fun areItemsTheSame(
                oldItem: ClassificationResult,
                newItem: ClassificationResult,
            ): Boolean {
                return oldItem == newItem
            }

            /**
             * Membandingkan konten dari dua ClassificationResult.
             * @param oldItem ClassificationResult lama.
             * @param newItem ClassificationResult baru.
             * @return True jika kontennya sama, false jika tidak.
             */
            override fun areContentsTheSame(
                oldItem: ClassificationResult,
                newItem: ClassificationResult,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * Membuat dan mengembalikan ViewHolder baru untuk RecyclerView.
     * @param parent ViewGroup induk tempat layout ViewHolder akan dipasang.
     * @param viewType Tipe tampilan item.
     * @return Instansi ViewHolder baru.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * Mengikat data ke ViewHolder di posisi tertentu dalam daftar.
     * @param holder ViewHolder yang akan mengikat data.
     * @param position Posisi item dalam daftar data.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        // Menetapkan listener klik untuk menavigasi ke ResultActivity
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_RESULT, item)
            context.startActivity(intent)
        }
    }
}