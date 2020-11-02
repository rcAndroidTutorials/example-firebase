package com.youtube.firebaserealtime.presentation.signup

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
import com.youtube.firebaserealtime.extensions.snack

class SignUpFragment : Fragment() {
    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordRepeat: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(view)
        setupViewModel()
    }
    private fun setup(view: View) {
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        etPasswordRepeat = view.findViewById(R.id.etPasswordRepeat)
        view.findViewById<Button>(R.id.btSignup).setOnClickListener {
            viewModel.create(
                etUsername.text.toString(),
                etPassword.text.toString(),
                etPasswordRepeat.text.toString()
            )
        }
    }
    private fun setupViewModel() {
        with(viewModel) {
            successLD.observe(viewLifecycleOwner) {
                activity?.also { it.snack(R.string.signup_success) }
                findNavController().navigateUp()
            }
            errorLD.observe(viewLifecycleOwner) { messageID ->
                activity?.also { it.snack(messageID) }
            }
        }
    }
}