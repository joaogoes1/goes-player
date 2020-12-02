package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.data.repository.music.MusicRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class ArtistListActions(
    private val musicRepository: MusicRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.ARTIST

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        val artists = musicRepository.getAllArtists().filter {
            !it.contains("unknown", ignoreCase = true)
        }
        list.postValue(artists)
    }

    override suspend fun openItem(name: String) {

    }
}