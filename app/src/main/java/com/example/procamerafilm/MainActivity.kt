package com.example.procamerafilm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var captureButton: Button
    private lateinit var recordButton: Button
    private lateinit var filmModeButton: Button
    private lateinit var settingsButton: Button
    private lateinit var timerText: TextView
    private lateinit var cameraProvider: ProcessCameraProvider
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var isRecording = false
    private var isFilmMode = false
    private var recordingStartTime = 0L
    
    private val cameraExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private val PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        requestPermissions()
    }

    private fun initViews() {
        previewView = findViewById(R.id.previewView)
        captureButton = findViewById(R.id.captureButton)
        recordButton = findViewById(R.id.recordButton)
        filmModeButton = findViewById(R.id.filmModeButton)
        settingsButton = findViewById(R.id.settingsButton)
        timerText = findViewById(R.id.timerText)

        captureButton.setOnClickListener { takePicture() }
        recordButton.setOnClickListener { toggleRecording() }
        filmModeButton.setOnClickListener { toggleFilmMode() }
        settingsButton.setOnClickListener { 
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
            }
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()

            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this, "Kamera başlatılamadı", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture() {
        Toast.makeText(this, "Fotoğraf çekildi", Toast.LENGTH_SHORT).show()
    }

    private fun toggleRecording() {
        if (!isRecording) {
            startRecording()
        } else {
            stopRecording()
        }
    }

    private fun startRecording() {
        val videoCapture = videoCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, name)
        }

        val outputOptions = FileOutputOptions.Builder(
            contentResolver,
            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        recording = videoCapture.output
            .prepareRecording(this, outputOptions)
            .withAudioEnabled()
            .start(cameraExecutor) {
                when (it) {
                    is VideoRecordEvent.Start -> {
                        isRecording = true
                        recordingStartTime = System.currentTimeMillis()
                        recordButton.text = "◼ Dur"
                        recordButton.setBackgroundColor(Color.RED)
                        startTimerUpdate()
                    }
                    is VideoRecordEvent.Finalize -> {
                        isRecording = false
                        recordButton.text = "● Kayıt"
                        recordButton.setBackgroundColor(Color.parseColor("#4CAF50"))
                        timerText.text = "00:00"
                    }
                }
            }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
    }

    private fun toggleFilmMode() {
        isFilmMode = !isFilmMode
        if (isFilmMode) {
            filmModeButton.setBackgroundColor(Color.parseColor("#FF6F00"))
            filmModeButton.text = "🎬 Film Mode: ON"
            Toast.makeText(this, "Film Mode Aktif - Sahne Takibi Başladı", Toast.LENGTH_SHORT).show()
        } else {
            filmModeButton.setBackgroundColor(Color.parseColor("#666666"))
            filmModeButton.text = "🎬 Film Mode"
            Toast.makeText(this, "Film Mode Kapalı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimerUpdate() {
        Thread {
            while (isRecording) {
                val elapsed = System.currentTimeMillis() - recordingStartTime
                val seconds = elapsed / 1000
                val minutes = seconds / 60
                timerText.post {
                    timerText.text = String.format("%02d:%02d", minutes, seconds % 60)
                }
                Thread.sleep(100)
            }
        }.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}

import android.content.ContentValues
