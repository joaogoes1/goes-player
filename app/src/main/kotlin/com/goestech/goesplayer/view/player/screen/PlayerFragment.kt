package com.goestech.goesplayer.view.player.screen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.goestech.goesplayer.R
import com.goestech.goesplayer.data.entity.Music
import com.goestech.goesplayer.databinding.PlayerFragmentBinding
import com.goestech.goesplayer.view.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class PlayerFragment : Fragment() {

    private val viewModel: PlayerFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PlayerFragmentBinding.inflate(inflater, container, false).apply {
            initializeButtons(this)
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
            viewModel.music.observe(viewLifecycleOwner, Observer {
                playerFragmentArtistName.text = it.artist
                playerFragmentMusicName.text = it.title ?: it.displayName ?: it.fileName
                playerFragmentTotalTime.text = it.duration.toMinutesAndSecondsText()
                    Glide
                    .with(root.context)
                    .load(Uri.parse(it.albumArtUri))
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(playerFragmentAlbumImage)
            })
            viewModel.position.observe(viewLifecycleOwner, {
                playerFragmentCurrentTime.text = it.toString()
                playerFragmentProgressBar.progress = it.toInt()
            })
            playerFragmentProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {

                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
                /*
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        currentTimeText.setText(gerarTempo(progress * 1000));
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                        Date date = new Date(progress * 1000);
                        currentTimeText.setText(sdf.format(date));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int pos = seekBar.getProgress() * 1000;
                    musicSrv.seekTo(pos);
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    Date date = new Date(musicSrv.mediaPlayer.getCurrentPosition());
                    currentTimeText.setText(sdf.format(date));
                }
                 */
            })
            // Implementar Progress bar
        }
    }

    private fun Long.toMinutesAndSecondsText(): String {
        val sdf = SimpleDateFormat("mm:ss")
        val date = Date(this * 1000)
        return sdf.format(date)
    }
}