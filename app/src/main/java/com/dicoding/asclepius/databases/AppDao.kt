package com.dicoding.asclepius.databases

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.ClassificationResult

/**
 * Data Access Object (DAO) untuk mengakses data pada tabel 'classificationresult'.
 * Menyediakan operasi untuk mengambil, menambah, dan menghapus data.
 */
@Dao
interface AppDao {

    /**
     * Mengambil semua data ClassificationResult yang tersimpan di database, diurutkan berdasarkan timestamp.
     * Data dikembalikan sebagai LiveData untuk pemantauan perubahan.
     * @return LiveData yang berisi daftar ClassificationResult.
     */
    @Query("SELECT * FROM classificationresult ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<ClassificationResult>>

    /**
     * Mengambil data ClassificationResult berdasarkan ID.
     * Data dikembalikan sebagai LiveData untuk pemantauan perubahan.
     * @param id ID dari ClassificationResult yang ingin diambil.
     * @return LiveData yang berisi ClassificationResult dengan ID tertentu.
     */
    @Query("SELECT * FROM classificationresult WHERE id = :id")
    fun getClassificationResultById(id: Int): LiveData<ClassificationResult>

    /**
     * Menyimpan satu atau lebih data ClassificationResult ke dalam database.
     * Jika ada data yang bertabrakan (conflict), data yang baru akan menggantikan data yang lama.
     * @param classificationResults Data ClassificationResult yang akan disimpan.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg classificationResults: ClassificationResult)

    /**
     * Menghapus data ClassificationResult dari database.
     * @param classificationResult Data ClassificationResult yang akan dihapus.
     */
    @Delete
    suspend fun delete(classificationResult: ClassificationResult)
}