package com.goesplayer.presentation.musiclist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.goesplayer.data.model.Music
import com.goesplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MusicListViewState {
    data object Loading : MusicListViewState()
    data object Error : MusicListViewState()
    data class Success(val musicList: List<Music>) : MusicListViewState()
}

@HiltViewModel
class MusicListViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
) : ViewModel() {

    private val _viewState = MutableLiveData<MusicListViewState>(MusicListViewState.Loading)
    val viewState: LiveData<MusicListViewState> = liveData { _viewState }
}