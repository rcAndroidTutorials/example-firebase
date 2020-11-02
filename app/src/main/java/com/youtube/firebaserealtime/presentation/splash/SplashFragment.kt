package com.youtube.firebaserealtime.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.popAnimation
import com.youtube.firebaserealtime.extensions.slideAnimation

class SplashFragment : Fragment() {
    private val viewModel by viewModels<SplashViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.finishLD.observe(viewLifecycleOwner) { isUserLogged ->
            if (isUserLogged) {
                findNavController().navigate(
                    SplashFragmentDirections.actionToCats(),
                    popAnimation(R.id.splashFragment).build()
                )
            } else {
                findNavController().navigate(
                    SplashFragmentDirections.actionToLogin(),
                    popAnimation(R.id.splashFragment).build()
                )
            }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }
}
