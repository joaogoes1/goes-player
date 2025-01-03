package com.goesplayer.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goesplayer.data.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {
    private val _playlistTabViewState =
        MutableLiveData<PlaylistTabViewState>(PlaylistTabViewState.Loading)
    val playlistTabViewState: LiveData<PlaylistTabViewState> = _playlistTabViewState

    fun deletePlaylist(playlistId: Long): Boolean {
        val result = playlistRepository.deletePlaylist(playlistId)
        if (result) loadPlaylists()
        return result
    }

    fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = playlistRepository.loadPlaylists()
            _playlistTabViewState.postValue(PlaylistTabViewState.Success(results))
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistRepository.createPlaylist(playlistName)
            if (result) loadPlaylists()
        }
    }
}