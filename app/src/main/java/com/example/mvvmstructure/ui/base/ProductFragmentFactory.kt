package com.example.mvvmstructure.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.ui.home.HomeFragment
import com.example.mvvmstructure.ui.home.adapters.ProductAdapter
import javax.inject.Inject

class ProductFragmentFactory @Inject constructor(
    private val productAdapter: ProductAdapter,
    private val glide: RequestManager,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            HomeFragment::class.java.name -> HomeFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}