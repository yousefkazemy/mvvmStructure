package com.example.mvvmstructure.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mvvmstructure.databinding.FragmentLoginBinding
import com.example.mvvmstructure.ui.base.BaseFragment
import com.example.mvvmstructure.utils.Resource
import com.example.mvvmstructure.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    lateinit var viewModel: LoginViewModel

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateFields(
                    email = binding.username.text.toString(),
                    password = binding.password.text.toString()
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.username.addTextChangedListener(editTextListener)
        binding.password.addTextChangedListener(editTextListener)

        binding.login.setOnClickListener {
            viewModel.login(
                email = binding.username.text.toString(),
                password = binding.password.text.toString()
            )
        }

        viewModel.loginBtnStatus.observe(viewLifecycleOwner, Observer {
            binding.login.isEnabled = it
        })

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { loginStatus: Resource<Boolean> ->
                if (loginStatus.status == Status.SUCCESS) {
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), loginStatus.message!!, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}