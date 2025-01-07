package com.goesplayer.presentation

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.PICTURE_TYPE_FRONT_COVER
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.goesplayer.data.model.Music
import com.goesplayer.data.repository.MusicRepository
import com.goesplayer.presentation.components.retrieveImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlayerViewState {
    data object None : PlayerViewState()
    data object Error : PlayerViewState()
    data object Loading : PlayerViewState()
    data class Success(
        val songName: String,
        val artist: String,
        val album: Uri?,
        val durationInMs: Long,
        val isPlaying: Boolean,
        val isShuffleEnabled: Boolean,
        val repeatMode: @Player.RepeatMode Int,
    ) : PlayerViewState()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    var controller: MediaController? = null
    val songList: MutableLiveData<List<Music>> = MutableLiveData()
    private val _playerViewState: MutableLiveData<PlayerViewState> =
        MutableLiveData(PlayerViewState.None)
    val playerViewState: LiveData<PlayerViewState>
        get() = _playerViewState.distinctUntilChanged()

    private val _playerProgress = MutableLiveData<Long>()
    val playerProgress: LiveData<Long>
        get() = _playerProgress.distinctUntilChanged()

    fun loadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            songList.postValue(musicRepository.loadSongs())
        }
    }

    fun playSong(music: Music) {
        controller?.apply {
            if (mediaMetadata.title == music.title && mediaMetadata.artist == music.artist) {
                return@apply
            }
            setMediaItem(music.toMediaItem(context))
            prepare()
            play()
        }
    }

    fun playOrPause() {
        controller?.let { controller ->
            if (controller.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        }
    }

    fun playMusicList(list: List<Music>) {
        controller?.apply {
            clearMediaItems()
            addMediaItems(
                list.map { it.toMediaItem(context) }
            )
            skipToNext()
            prepare()
            play()
        }
    }

    fun changeShuffleState() {
        controller?.apply {
            shuffleModeEnabled = !shuffleModeEnabled
        }
    }

    fun changeRepeatState() {
        controller?.apply {
            repeatMode = when (repeatMode) {
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }
        }
    }

    fun skipToPrevious() {
        controller?.seekToPrevious()
    }

    fun skipToNext() {
        controller?.seekToNext()
    }

    fun updatePlayerStatus(viewState: PlayerViewState) {
        _playerViewState.postValue(viewState)
    }

    fun changeProgress(position: Long) {
        controller?.seekTo(position)
    }

    fun getPosition(): Long = controller?.contentPosition ?: 0
}

@OptIn(UnstableApi::class)
private fun Music.toMediaItem(context: Context) =
    MediaItem
        .Builder()
        .setUri(songUri)
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setArtworkData(MediaMetadataRetriever().let {
                    it.setDataSource(context, albumArtUri)
                    it.embeddedPicture
                }, PICTURE_TYPE_FRONT_COVER)
                .setArtworkUri(albumArtUri)
                .build()
        ).build()
