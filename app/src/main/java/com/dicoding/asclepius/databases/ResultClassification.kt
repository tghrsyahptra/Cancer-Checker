package com.dicoding.asclepius.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * ClassificationResult is an entity class that represents a classification result.
 * It is used to store information about the image URI, label, score, and timestamp
 * of a classification in the local database. This class is also parcelable,
 * which allows it to be passed between components like Activities or Fragments.
 */
@Entity
@Parcelize
data class ClassificationResult(
    /**
     * id is the unique identifier of the classification result.
     * It is auto-generated by the Room database.
     */
    @PrimaryKey(autoGenerate = true) val id: Int? = null,

    /**
     * timestamp represents the time when the classification occurred.
     * Stored as a Long value in milliseconds.
     */
    @ColumnInfo(name = "timestamp") val timestamp: Long,

    /**
     * imageUri is the URI (Uniform Resource Identifier) of the image associated
     * with the classification result.
     */
    @ColumnInfo(name = "image_uri") val imageUri: String,

    /**
     * label represents the classification label assigned to the image.
     * This is a textual representation of the predicted class.
     */
    @ColumnInfo(name = "label") val label: String,

    /**
     * score represents the confidence score of the classification.
     * It is a floating point number between 0 and 1.
     */
    @ColumnInfo(name = "score") val score: Float
) : Parcelable