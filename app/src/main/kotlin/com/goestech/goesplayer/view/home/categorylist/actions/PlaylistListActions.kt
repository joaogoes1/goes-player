package com.goestech.goesplayer.view.home.categorylist.actions

import androidx.lifecycle.MutableLiveData
import com.goestech.goesplayer.data.repository.playlist.PlaylistRepository
import com.goestech.goesplayer.view.home.categorylist.CategoryListType

class PlaylistListActions(
    private val playlistRepository: PlaylistRepository
) : CategoryListViewModelActions {
    override val type: CategoryListType = CategoryListType.PLAYLIST

    override suspend fun loadList(list: MutableLiveData<List<String>>) {
        val playlists = playlistRepository.getAllPlaylists()
        list.postValue(playlists)
    }

    override suspend fun openItem(name: String) {
        // TODO("Not yet implemented")
    }
}