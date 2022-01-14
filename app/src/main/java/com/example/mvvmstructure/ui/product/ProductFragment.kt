package com.example.mvvmstructure.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.R
import com.example.mvvmstructure.databinding.FragmentProductBinding
import com.example.mvvmstructure.ui.base.BaseFragment
import com.example.mvvmstructure.utils.screen.ScreenUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : BaseFragment() {

    private lateinit var viewModel: ProductViewModel

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val args = ProductFragmentArgs.fromBundle(it)
            viewModel.fetchProduct(args.id)
        }

        viewModel.product.observe(viewLifecycleOwner, {
            binding.layoutProduct.textUserName.text = it.username

            val params: ViewGroup.LayoutParams =
                binding.layoutProduct.frameMediaContainer.layoutParams
            val placeholderWidth = ScreenUtils.getScreenWidth()
            val placeholderHeight = ScreenUtils.calculatePlaceHolderHeight(
                it.imageWidth,
                it.imageHeight
            )
            params.width = placeholderWidth
            params.height = placeholderHeight
            binding.layoutProduct.frameMediaContainer.layoutParams = params

            glide.load(it.imageUrl).into(binding.layoutProduct.imageThumbnail)

            if (it.views > 1) {
                "${it.views} ${
                    getString(R.string.views)
                }".also { binding.layoutProduct.textViewsCount.text = it }
            } else {
                "${it.views} ${
                    getString(R.string.view)
                }".also { binding.layoutProduct.textViewsCount.text = it }
            }

            binding.layoutProduct.textDescription.text = it.tags
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}