package com.goestech.goesplayer.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.goestech.goesplayer.R
import com.goestech.goesplayer.databinding.SplashFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = SplashFragmentBinding.inflate(inflater).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeStatus()
        viewModel.initApp()
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, {
            if (it == InitializationStatus.READY) {
                onFinishInitialization()
            }
        })
    }

    private fun onFinishInitialization() {
        findNavController()
            .navigate(R.id.action_splashFragment_to_homeFragment,
                null,
                NavOptions
                    .Builder()
                    .setPopUpTo(R.id.splashFragment, true)
                    .build()
            )
    }
}