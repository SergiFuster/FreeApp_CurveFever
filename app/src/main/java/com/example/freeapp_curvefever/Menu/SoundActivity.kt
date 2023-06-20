package com.example.freeapp_curvefever.Menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import com.example.freeapp_curvefever.R

class SoundActivity : AppCompatActivity() {
    lateinit var soundSwitch : Switch
    lateinit var menuButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)
        soundSwitch = findViewById(R.id.soundSwitch)
        menuButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener { menuButtonPressed() }
    }

    private fun menuButtonPressed() {
        val intent = Intent(
            this,
            MenuActivity::class.java
        ).apply {
            putExtra(SOUND, soundSwitch.isChecked)
        }
        startActivity(intent)
    }

    companion object{
        const val SOUND = "SOUND"
    }
}