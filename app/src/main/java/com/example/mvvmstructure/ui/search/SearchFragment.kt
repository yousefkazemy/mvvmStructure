package com.example.mvvmstructure.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmstructure.databinding.FragmentSearchBinding
import com.example.mvvmstructure.ui.base.BaseFragment
import com.example.mvvmstructure.ui.home.adapters.ProductAdapter
import com.example.mvvmstructure.utils.Status
import com.example.mvvmstructure.utils.rx.rxEditTextSearchObservable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchForProduct(rxEditTextSearchObservable(binding.editSearch))

        binding.recyclerResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        viewModel.searchField.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.editSearch.setText(it)
            }
        })

        viewModel.products.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.textResultLabel.visibility = View.INVISIBLE
                        binding.shimmerFrameLayout.visibility = View.INVISIBLE
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.recyclerResult.visibility = View.VISIBLE
                        productAdapter.setData(it.data)
                    } else {
                        binding.shimmerFrameLayout.visibility = View.INVISIBLE
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.recyclerResult.visibility = View.INVISIBLE
                        binding.textResultLabel.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    binding.recyclerResult.visibility = View.INVISIBLE
                    binding.textResultLabel.visibility = View.INVISIBLE
                    binding.shimmerFrameLayout.visibility = View.VISIBLE
                    binding.shimmerFrameLayout.startShimmer()
                }
                else -> {
                    // TODO ==> Should handle error
                }
            }
        })

        viewModel.recyclerPosition.observe(viewLifecycleOwner, {
            if (productAdapter.itemCount > 0) {
                binding.recyclerResult.scrollToPosition(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setSearchField(binding.editSearch.text.toString())
        if (productAdapter.itemCount > 0) {
            val layoutManager = binding.recyclerResult.layoutManager as LinearLayoutManager
            viewModel.setRecyclerPosition(layoutManager.findFirstVisibleItemPosition())
        }
        _binding = null
    }
}