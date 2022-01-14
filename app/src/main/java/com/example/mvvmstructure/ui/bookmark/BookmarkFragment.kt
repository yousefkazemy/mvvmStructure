package com.example.mvvmstructure.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmstructure.databinding.FragmentBookmarkBinding
import com.example.mvvmstructure.ui.base.BaseFragment
import com.example.mvvmstructure.ui.bookmark.adapters.BookmarkedProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : BaseFragment() {

    private lateinit var viewModel: BookmarkViewModel

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bookmarkedProductAdapter: BookmarkedProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(BookmarkViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bookmarkedProductAdapter
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            bookmarkedProductAdapter.setData(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}