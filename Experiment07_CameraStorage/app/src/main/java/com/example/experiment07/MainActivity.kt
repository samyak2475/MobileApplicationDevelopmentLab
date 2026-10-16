
package com.example.experiment07

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var tvStatus: TextView

    private val captureImage =
        registerForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->
            if (bitmap != null) {
                saveImage(bitmap)
            } else {
                Toast.makeText(
                    this,
                    "No image captured",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagePreview = findViewById(R.id.imagePreview)
        tvStatus = findViewById(R.id.tvStatus)

        findViewById<Button>(R.id.btnCapture).setOnClickListener {
            captureImage.launch(null)
        }

        findViewById<Button>(R.id.btnViewSaved).setOnClickListener {
            loadSavedImage()
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        try {
            val file = File(filesDir, "captured_image.jpg")

            FileOutputStream(file).use { output ->
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    output
                )
            }

            imagePreview.setImageBitmap(bitmap)
            tvStatus.text =
                "Image saved successfully in internal storage."

            Toast.makeText(
                this,
                "Image saved",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            tvStatus.text = "Failed to save image: ${e.message}"
        }
    }

    private fun loadSavedImage() {
        val file = File(filesDir, "captured_image.jpg")

        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imagePreview.setImageBitmap(bitmap)
            tvStatus.text = "Loaded saved image: ${file.name}"
        } else {
            tvStatus.text = "No saved image found. Capture an image first."
        }
    }
}
