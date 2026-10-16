package com.example.experiment09

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnPlay.setOnClickListener {
            sendMusicCommand(MusicService.ACTION_PLAY)
        }

        btnPause.setOnClickListener {
            sendMusicCommand(MusicService.ACTION_PAUSE)
        }

        btnStop.setOnClickListener {
            sendMusicCommand(MusicService.ACTION_STOP)
        }
    }

    private fun sendMusicCommand(action: String) {
        val intent = Intent(this, MusicService::class.java)
        intent.action = action

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) &&
            (action == MusicService.ACTION_PLAY)
        ) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
