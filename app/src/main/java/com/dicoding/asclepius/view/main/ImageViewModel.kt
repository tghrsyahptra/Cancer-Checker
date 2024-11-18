package com.dicoding.asclepius.view.main

import android.net.Uri
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {
    var currentImageUri: Uri? = null
    var croppedImageUri: Uri? = null
}