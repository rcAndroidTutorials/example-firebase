package com.youtube.firebaserealtime.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.popAnimation
import com.youtube.firebaserealtime.extensions.slideAnimation
import com.youtube.firebaserealtime.extensions.snack

class LoginFragment : Fragment() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(view)
        setupViewModel()
    }
    private fun setup(view: View) {
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        view.findViewById<Button>(R.id.btSignup).setOnClickListener { viewModel.onSignUpPressed() }
        view.findViewById<Button>(R.id.btLogin).setOnClickListener {
            viewModel.login(etUsername.text.toString(), etPassword.text.toString())
        }
    }
    private fun setupViewModel() {
        with(viewModel) {
            signUpLD.observe(viewLifecycleOwner) { openSignup() }
            successLD.observe(viewLifecycleOwner) {
                activity?.also { it.snack(R.string.login_success) }
                openCats()
            }
            errorLD.observe(viewLifecycleOwner) { message ->
                activity?.also { it.snack(message) }
            }
        }
    }
    private fun openSignup() {
        findNavController().navigate(
            LoginFragmentDirections.actionToSignUp(),
            slideAnimation().build()
        )
    }
    private fun openCats() {
        findNavController().navigate(
            LoginFragmentDirections.actionToCats(),
            popAnimation(R.id.loginFragment).build()
        )
    }
}
