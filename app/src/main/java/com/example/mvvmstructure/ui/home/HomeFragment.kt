package com.example.mvvmstructure.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmstructure.databinding.FragmentHomeBinding
import com.example.mvvmstructure.ui.base.BaseFragment
import com.example.mvvmstructure.ui.home.adapters.ProductAdapter
import com.example.mvvmstructure.utils.Status
import com.example.mvvmstructure.utils.adapter.OnBookmarkItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnBookmarkItemClickListener {

    lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productAdapter.setOnBookmarkItemClickListener(this)
        binding.recyclerProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (binding.recyclerProducts.visibility == View.INVISIBLE) {
                        binding.shimmerFrameLayout.stopShimmer()
                        binding.shimmerFrameLayout.visibility = View.INVISIBLE
                        binding.recyclerProducts.visibility = View.VISIBLE
                    }
                    productAdapter.setData(it.data!!)
                }
                Status.LOADING -> {
                    binding.shimmerFrameLayout.startShimmer()
                }
                else -> {
                    // TODO ==> Handle error
                }
            }
        })

        viewModel.recyclerPosition.observe(viewLifecycleOwner, {
            if (productAdapter.itemCount > 0) {
                binding.recyclerProducts.scrollToPosition(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (productAdapter.itemCount > 0) {
            val layoutManager = binding.recyclerProducts.layoutManager as LinearLayoutManager
            viewModel.setRecyclerPosition(layoutManager.findFirstVisibleItemPosition())
        }
        _binding = null
    }

    override fun onBookmarkClick(position: Int) {
        viewModel.changeProductBookmark(position)
    }
}