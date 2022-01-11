package com.example.textrecognition.view

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.textrecognition.R
import com.example.textrecognition.preference.BitmapUtils
import com.example.textrecognition.preference.GraphicOverlay
import com.example.textrecognition.preference.VisionImageProcessor
import com.example.textrecognition.textDetector.TextRecognitionProcessor
import com.example.textrecognition.view.details_receipt.DetailsReceiptAcivity
import com.example.textrecognition.view.list_receipt.ListReceiptActivity
import com.example.textrecognition.view.list_sync_receipt.ListSyncReceiptActivity
import com.example.textrecognition.worker.AlarmReceiver
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1001
    private val REQUEST_CHOOSE_IMAGE = 1002
    private var imageUri: Uri? = null
    private val SIZE_SCREEN = "w:screen" // Match screen width
    private var selectedSize: String? = SIZE_SCREEN
    private var imageMaxWidth = 0
    private var imageProcessor: VisionImageProcessor? = null
    private var preview: ImageView? = null
    private var btnChoseFile: AppCompatButton? = null
    private var imageMaxHeight = 0
    private var graphicOverlay: GraphicOverlay? = null
    private val TAG = "MainActivity"
    private val SIZE_ORIGINAL = "w:original"
    private val SIZE_1024_768 = "w:1024"
    private val SIZE_640_480 = "w:640"
    private var isLandScape = false
    val UUID_USER = "uuid_user"
    lateinit var uuidValue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preview = findViewById(R.id.preview)
        graphicOverlay = findViewById(R.id.graphic_overlay)

        btnChoseFile = findViewById(R.id.btnChoseFile)

        btnChoseFile?.setOnClickListener {
            startChooseImageIntentForResult()
        }

        isLandScape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        val rootView = findViewById<View>(R.id.root)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    imageMaxWidth = rootView.width
                    imageMaxHeight =
                        rootView.height - findViewById<View>(R.id.control).height
                    if (SIZE_SCREEN == selectedSize) {
                        tryReloadAndDetectInImage()
                    }
                }
            })

        if (getUserUUID().isEmpty())
            storeUserUUID()
        createWorkManager()
    }

    private fun storeUserUUID() {
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(UUID_USER, UUID.randomUUID().toString())
            commit()
        }
        uuidValue = sharedPref.getString(UUID_USER, "").orEmpty()
    }

    private fun getUserUUID(): String {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        uuidValue = sharedPref.getString(UUID_USER, "").orEmpty()
        return uuidValue
    }

    private fun createWorkManager() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                0
            )

        val timeInterval = 60 * 1_0000L
        val alarmTime = System.currentTimeMillis() + 5_000L
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, timeInterval , pendingIntent)
    }

    private val targetedWidthHeight: Pair<Int, Int>
        get() {
            val targetWidth: Int
            val targetHeight: Int
            when (selectedSize) {
                SIZE_SCREEN -> {
                    targetWidth = imageMaxWidth
                    targetHeight = imageMaxHeight
                }
                SIZE_640_480 -> {
                    targetWidth = if (isLandScape) 640 else 480
                    targetHeight = if (isLandScape) 480 else 640
                }
                SIZE_1024_768 -> {
                    targetWidth = if (isLandScape) 1024 else 768
                    targetHeight = if (isLandScape) 768 else 1024
                }
                else -> throw IllegalStateException("Unknown size")
            }
            return Pair(targetWidth, targetHeight)
        }

    private fun startChooseImageIntentForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            REQUEST_CHOOSE_IMAGE
        )
    }

    private fun createImageProcessor() {
        try {
            imageProcessor =
                TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build(), uuidValue)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Can not create image processor",
                e
            )
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message,
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        createImageProcessor()
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            tryReloadAndDetectInImage()
        } else if (requestCode == REQUEST_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // In this case, imageUri is returned by the chooser, save it.
            imageUri = data!!.data
            tryReloadAndDetectInImage()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.text_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_item -> {
                startActivity(Intent(this, ListReceiptActivity::class.java))
                true
            }
            R.id.list_item_sync -> {
                val i = Intent(this, ListSyncReceiptActivity::class.java)
                i.putExtra("uuid_user", uuidValue)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun tryReloadAndDetectInImage() {
        Log.d(
            TAG,
            "Try reload and detect image"
        )
        try {
            if (imageUri == null) {
                return
            }

            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
                // UI layout has not finished yet, will reload once it's ready.
                return
            }

            val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri)
                ?: return
            // Clear the overlay first
            graphicOverlay!!.clear()

            val resizedBitmap: Bitmap
            resizedBitmap = if (selectedSize == SIZE_ORIGINAL) {
                imageBitmap
            } else {
                // Get the dimensions of the image view
                val targetedSize: Pair<Int, Int> = targetedWidthHeight

                // Determine how much to scale down the image
                val scaleFactor = Math.max(
                    imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
                    imageBitmap.height.toFloat() / targetedSize.second.toFloat()
                )
                Bitmap.createScaledBitmap(
                    imageBitmap,
                    (imageBitmap.width / scaleFactor).toInt(),
                    (imageBitmap.height / scaleFactor).toInt(),
                    true
                )
            }

            preview?.let {
                Glide.with(this)
                    .load(resizedBitmap)
                    .into(it)
            }

            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)
            preview?.colorFilter = filter

            if (imageProcessor != null) {
                graphicOverlay!!.setImageSourceInfo(
                    resizedBitmap.width, resizedBitmap.height, /* isFlipped= */false
                )
                imageProcessor!!.processBitmap(resizedBitmap, graphicOverlay)
            } else {
                Log.e(
                    TAG,
                    "Null imageProcessor, please check adb logs for imageProcessor creation error"
                )
            }
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Error retrieving saved image"
            )
            imageUri = null
        }
    }
}