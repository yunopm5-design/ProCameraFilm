package com.example.procamerafilm

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.backButton)
        val qualitySwitch = findViewById<Switch>(R.id.qualitySwitch)
        val audioSwitch = findViewById<Switch>(R.id.audioSwitch)
        val filmModeSwitch = findViewById<Switch>(R.id.filmModeSwitch)
        val versionText = findViewById<TextView>(R.id.versionText)

        backButton.setOnClickListener { finish() }
        
        versionText.text = "Pro Kamera Film v1.0\nBy Yuno"
    }
}
