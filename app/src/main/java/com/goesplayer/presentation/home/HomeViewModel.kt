package com.goesplayer.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.goesplayer.data.model.Music
import com.goesplayer.data.model.Playlist
import com.goesplayer.data.model.PlaylistWithMusics
import com.goesplayer.data.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
) : ViewModel() {
    private val _playlistTabViewState =
        MutableLiveData<PlaylistTabViewState>(PlaylistTabViewState.Loading)
    val playlistTabViewState: LiveData<PlaylistTabViewState> =
        _playlistTabViewState.distinctUntilChanged()

    fun deletePlaylist(playlistId: Long): Boolean {
        try {
            viewModelScope.launch {
                playlistRepository.deletePlaylist(playlistId)
                playlistRepository.loadPlaylists().apply {
                    _playlistTabViewState.postValue(PlaylistTabViewState.Success(this))
                }
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val results = playlistRepository.loadPlaylists()
                _playlistTabViewState.postValue(PlaylistTabViewState.Success(results))
            } catch (e: Exception) {
                _playlistTabViewState.postValue(PlaylistTabViewState.Error)
            }
        }
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                playlistRepository.createPlaylist(playlistName)
                loadPlaylists()
            } catch (_: Exception) {
            }
        }
    }

    fun addMusicToPlaylist(music: Music, playlist: Playlist): Boolean {
        try {
            viewModelScope.launch {
                playlistRepository.addToPlaylist(music, playlist)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun getPlaylist(): Flow<SearchPlaylistsState> = flow {
        emit(SearchPlaylistsState.Loading)
        try {
            emit(SearchPlaylistsState.Success(playlistRepository.loadPlaylists()))
        } catch (e: Exception) {
            emit(SearchPlaylistsState.Error)
        }
    }

    fun getPlaylistDetails(): Flow<PlaylistWithMusics> = flow {

    }
}

sealed class SearchPlaylistsState {
    data object Error : SearchPlaylistsState()
    data object Loading : SearchPlaylistsState()
    data class Success(val list: List<Playlist>) : SearchPlaylistsState()
}
