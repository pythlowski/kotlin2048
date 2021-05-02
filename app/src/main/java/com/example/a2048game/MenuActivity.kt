package com.example.a2048game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MenuActivity : AppCompatActivity() {

    internal lateinit var playButton: Button
    internal lateinit var highScoresButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        playButton = findViewById(R.id.playButton)
        highScoresButton = findViewById(R.id.highScoresButton)

        playButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply{
            }
            startActivity(intent)
        }

        highScoresButton.setOnClickListener {
            val intent = Intent(this, HighScoresActivity::class.java).apply{
            }
            startActivity(intent)
        }
    }
}