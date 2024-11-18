package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

/**
 * Kelas ImageClassifierHelper digunakan untuk melakukan klasifikasi gambar
 * menggunakan model TensorFlow Lite. Kelas ini menyediakan metode untuk mengklasifikasikan gambar statis
 * dan memberikan hasil klasifikasi ke listener.
 */
class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener?,
    private var threshold: Float = 0.1f, // Ambang batas untuk klasifikasi
    private var maxResults: Int = 2, // Jumlah hasil klasifikasi yang ingin ditampilkan
    private val modelName: String = "cancer_classification.tflite", // Nama model TensorFlow Lite
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        // Inisialisasi Image Classifier saat objek dibuat
        initializeImageClassifier()
    }

    /**
     * Fungsi untuk mengonfigurasi dan membuat instance ImageClassifier.
     * Fungsi ini akan dipanggil pada saat pertama kali kelas diinisialisasi.
     */
    private fun initializeImageClassifier() {
        // Membuat konfigurasi untuk ImageClassifier dengan opsi threshold dan maxResults
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)

        // Membuat opsi dasar untuk ImageClassifier
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4) // Menetapkan jumlah thread untuk inferensi
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            // Membuat instance ImageClassifier dari model yang diberikan
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context, modelName, optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            // Menangani kesalahan jika pembuatan ImageClassifier gagal
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    /**
     * Fungsi untuk mengklasifikasikan gambar statis menggunakan model yang telah diinisialisasi.
     * @param imageUri Uri gambar yang ingin diklasifikasikan
     */
    fun classifyStaticImage(imageUri: Uri) {
        // Memastikan ImageClassifier telah diinisialisasi
        if (imageClassifier == null) {
            initializeImageClassifier()
        }

        // Menyiapkan ImageProcessor untuk mengubah ukuran dan cast gambar menjadi tipe data FLOAT32
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR)) // Resize gambar
            .add(CastOp(DataType.FLOAT32)) // Ubah tipe data gambar menjadi FLOAT32
            .build()

        // Proses gambar dari URI menjadi tensor untuk inferensi
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(convertUriToBitmap(imageUri)))

        // Mulai pengukuran waktu inferensi
        var inferenceTime = SystemClock.uptimeMillis()

        // Menjalankan klasifikasi gambar dan mendapatkan hasilnya
        val results = imageClassifier?.classify(tensorImage)

        // Menghitung waktu yang dibutuhkan untuk inferensi
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        // Mengirimkan hasil dan waktu inferensi ke listener
        classifierListener?.onResults(results, inferenceTime)
    }

    /**
     * Fungsi untuk mengonversi Uri gambar menjadi Bitmap.
     * @param uri Uri gambar yang ingin dikonversi
     * @return Bitmap yang dihasilkan dari gambar Uri, atau null jika gagal
     */
    private fun convertUriToBitmap(uri: Uri): Bitmap? {
        return try {
            // Membuka stream input dari URI dan mengonversinya menjadi Bitmap
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            // Menangani kesalahan jika konversi gagal
            e.printStackTrace()
            Log.e(TAG, e.message.toString())
            null
        }
    }

    /**
     * Listener untuk menerima hasil klasifikasi atau kesalahan dari proses klasifikasi.
     */
    interface ClassifierListener {
        /**
         * Fungsi untuk menangani kesalahan selama proses klasifikasi.
         * @param error Pesan kesalahan
         */
        fun onError(error: String)

        /**
         * Fungsi untuk menangani hasil klasifikasi gambar.
         * @param results Daftar hasil klasifikasi
         * @param inferenceTime Waktu yang dibutuhkan untuk klasifikasi
         */
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
    }

    companion object {
        // Tag untuk log debugging
        private const val TAG = "ImageClassifierHelper"
    }
}