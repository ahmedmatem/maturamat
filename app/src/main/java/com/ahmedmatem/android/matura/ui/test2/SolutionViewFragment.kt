package com.ahmedmatem.android.matura.ui.test2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ahmedmatem.android.matura.base.BaseFragment
import com.ahmedmatem.android.matura.base.NavigationCommand
import com.ahmedmatem.android.matura.databinding.FragmentSolutionViewBinding
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_URI = "arg_uri"

/**
 * A simple [Fragment] subclass.
 * Use the [SolutionViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SolutionViewFragment : BaseFragment() {

    // Todo: Implement Zoom-In and Zoom-Out over image

    override val viewModel: SolutionViewViewModel by viewModels()

    private var _binding: FragmentSolutionViewBinding? = null
    private val binding: FragmentSolutionViewBinding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var uri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionViewBinding.inflate(inflater, container, false)

        binding.solutionImageView.let {
            Picasso.get().load(uri).into(it)
        }

        binding.closeBtn.setOnClickListener {
            viewModel.navigationCommand.value = NavigationCommand.Back
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param uri Parameter 1.
         * @return A new instance of fragment SolutionViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(uri: String) =
            SolutionViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }
}