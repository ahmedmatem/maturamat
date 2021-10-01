package com.ahmedmatem.android.matura.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ahmedmatem.android.matura.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textDashboard
//        viewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val loginBtn: Button = binding.button2
        val registerBtn: Button = binding.button3

        loginBtn.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionNavigationAccountToAuthNavigation())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}