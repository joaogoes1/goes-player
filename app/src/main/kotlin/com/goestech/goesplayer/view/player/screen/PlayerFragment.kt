package com.goesplayer.view.player.screen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.goesplayer.commons.extensions.formatToDigitalClock
import com.goesplayer.R
import com.goesplayer.databinding.PlayerFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class PlayerFragment : Fragment() {

    private val viewModel: PlayerFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PlayerFragmentBinding.inflate(inflater, container, false).apply {
            initializeButtons(this)
            observeViewModel(this)
        }.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    private fun initializeButtons(binding: PlayerFragmentBinding) {
        binding.apply {
            playerFragmentPlayButton.setOnClickListener {
                viewModel.playOrPause()
            }
            playerFragmentNextButton.setOnClickListener {
                viewModel.skipToNext()
            }
            playerFragmentPreviousButton.setOnClickListener {
                viewModel.skipToPrevious()
            }
            playerFragmentProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) viewModel.seekTo(progress * 1000)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            playerFragmentAlbumImage.setOnClickListener {
                if (playerFragmentAlbumImage.visibility == View.VISIBLE) {
                    val lyrics = viewModel.lyrics.value?.text
                    lyrics?.let {
                        playerFragmentLyricsText.text = it
                        playerFragmentAlbumImage.visibility = View.INVISIBLE
                        playerFragmentLyricsScroll.visibility = View.VISIBLE
                    }
                } else {
                    playerFragmentAlbumImage.visibility = View.VISIBLE
                    playerFragmentLyricsScroll.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun observeViewModel(binding: PlayerFragmentBinding) {
        binding.apply {
            viewModel.music.observe(viewLifecycleOwner, Observer {
                viewModel.loadLyrics()
                playerFragmentArtistName.text = it.artist
                playerFragmentMusicName.text = it.title ?: it.displayName ?: it.fileName
                playerFragmentTotalTime.text = it.duration.formatToDigitalClock()
                playerFragmentProgressBar.max = it.duration.toInt() / 1000
                Glide
                    .with(root.context)
                    .load(Uri.parse(it.albumArtUri))
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(playerFragmentAlbumImage)
            })
            viewModel.position.observe(viewLifecycleOwner, Observer {
                playerFragmentCurrentTime.text = it.formatToDigitalClock()
                setSeekBarProgress(playerFragmentProgressBar, it.toInt())
            })
            viewModel.isPlaying.observe(viewLifecycleOwner, Observer {
                val newImage = if (it)
                    R.drawable.ic_pause_black
                else
                    R.drawable.ic_play_black
                playerFragmentPlayButton.setImageResource(newImage)
            })
        }
    }

    private fun setSeekBarProgress(seekBar: SeekBar, progress: Int) {
        seekBar.progress = progress / 1000
    }

    private fun getSeekBarProgress(seekBar: SeekBar?): Int =
        seekBar?.progress ?: 0 * 1000
}