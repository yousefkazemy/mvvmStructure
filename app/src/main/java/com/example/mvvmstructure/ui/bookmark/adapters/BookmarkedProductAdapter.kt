package com.example.mvvmstructure.ui.bookmark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.databinding.ItemViewProductBookmarkBinding
import com.example.mvvmstructure.ui.bookmark.BookmarkFragmentDirections
import javax.inject.Inject

class BookmarkedProductAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val products = ArrayList<ProductEntity>()

    inner class ProductViewHolder(val binding: ItemViewProductBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val action = BookmarkFragmentDirections.actionBookmarkFragmentToProductFragment(
                    products[adapterPosition].id
                )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ProductViewHolder(
            ItemViewProductBookmarkBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = holder as ProductViewHolder
        glide.load(products[position].imageUrl).into(holder.binding.imageProduct)
    }

    override fun getItemCount(): Int = products.size

    fun setData(newData: List<ProductEntity>) {
        val diffResult =
            DiffUtil.calculateDiff(BookmarkedProductDiffUtilCallBack(newData, products))
        diffResult.dispatchUpdatesTo(this)
        products.clear()
        products.addAll(newData)
    }
}