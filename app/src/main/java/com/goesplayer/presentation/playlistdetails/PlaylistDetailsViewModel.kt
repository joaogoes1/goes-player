package com.goesplayer.presentation.playlistdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.goesplayer.data.model.Music
import com.goesplayer.data.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class PlaylistDetailsViewState {
    data object Error : PlaylistDetailsViewState()
    data object Empty : PlaylistDetailsViewState()
    data object Loading : PlaylistDetailsViewState()
    data class Success(val musicList: List<Music>) : PlaylistDetailsViewState()
}

@HiltViewModel
class PlaylistDetailsViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {
    private val _viewState = MutableLiveData<PlaylistDetailsViewState>(
        PlaylistDetailsViewState.Loading,
    )
    val viewState: LiveData<PlaylistDetailsViewState>
        get() = _viewState.distinctUntilChanged()

    fun loadDetails(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                playlistRepository.loadPlaylistMusics(playlistId).apply {
                    Log.e("ASDFGHJKL", this.toString())
                    val state = if (isEmpty())
                        PlaylistDetailsViewState.Empty
                    else
                        PlaylistDetailsViewState.Success(this)
                    _viewState.postValue(state)
                }
            } catch (_: Exception) {
                _viewState.postValue(PlaylistDetailsViewState.Error)
            }
        }
    }
}
