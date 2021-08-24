package com.goestech.goesplayer.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goesplayer.player.client.MediaPlayerClient
import com.goesplayer.player.service.PlayerService
import com.goestech.goesplayer.R
import com.goestech.goesplayer.databinding.HomeActivityBinding
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private var binding: HomeActivityBinding? = null
    private val playerClient: MediaPlayerClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        playerClient.onCreate()
    }

    override fun onStart() {
        super.onStart()
        playerClient.onStart()
    }

    override fun onStop() {
        super.onStop()
        playerClient.onStop()
    }
}