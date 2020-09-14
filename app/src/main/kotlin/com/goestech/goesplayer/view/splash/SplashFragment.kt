package com.goestech.goesplayer.view.splash

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.goestech.goesplayer.R
import com.goestech.goesplayer.databinding.SplashFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

private const val REQUEST_CODE = 123

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = SplashFragmentBinding.inflate(inflater).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeStatus()
        verifyPermissions()
    }

    private fun verifyPermissions() {
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).let { result ->
            if (result == PERMISSION_GRANTED) {
                viewModel.initApp()
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE
                && grantResults[0] == PERMISSION_GRANTED) {
                viewModel.initApp()
            }
        }
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, {
            if (it == InitializationStatus.READY) {
                navigateToHomeScreen()
            }
        })
    }

    private fun navigateToHomeScreen() {
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