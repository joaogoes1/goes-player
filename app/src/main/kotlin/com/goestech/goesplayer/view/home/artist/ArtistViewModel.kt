package com.goestech.goesplayer.view.home.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goestech.goesplayer.data.repository.music.MusicRepository
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository: MusicRepository
) : ViewModel() {
    val artists: MutableLiveData<List<String>> = MutableLiveData(emptyList())

    fun loadArtists() {
        viewModelScope.launch {
            val artistsList = repository.getAllArtists()
            artists.postValue(artistsList)
        }
    }
}