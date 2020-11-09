package com.goestech.goesplayer.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.goestech.goesplayer.data.entity.Music
import java.io.FileNotFoundException

private const val PLAYER_ERROR_TAG = "GOES PLAYER-MEDIAPLAYER"

class CustomPlayer(private val context: Context) : Player, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private val player: MediaPlayer = MediaPlayer()

    init {
        player.setOnPreparedListener(this)
    }

    override fun playFromMedia(music: MediaMetadataCompat) {
        player.reset()
        try {
            val uri = Uri.parse(music.getString(METADATA_KEY_PATH)) ?: throw FileNotFoundException(music.getString(music.toString()))
            player.setDataSource(uri.path)
            player.prepareAsync()
        } catch (e: Exception) {
            Log.e(PLAYER_ERROR_TAG, "Error on playFromMedia:\n\t" + e.message)
        }
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
        player.release()
    }

    override fun seekTo(pos: Int) {
        player.seekTo(pos)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e(PLAYER_ERROR_TAG, "What: $what\nExtra: $extra\n")
        mp?.reset()
        return true
    }
}
