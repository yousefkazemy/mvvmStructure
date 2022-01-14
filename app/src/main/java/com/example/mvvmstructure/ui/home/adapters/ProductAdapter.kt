package com.example.mvvmstructure.ui.home.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.R
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.databinding.ItemViewProductBinding
import com.example.mvvmstructure.utils.adapter.OnBookmarkItemClickListener
import com.example.mvvmstructure.utils.screen.ScreenUtils.calculatePlaceHolderHeight
import com.example.mvvmstructure.utils.screen.ScreenUtils.getScreenWidth
import javax.inject.Inject

class ProductAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val products = ArrayList<Product>()

    fun getProducts(): List<Product> {
        return products
    }

    lateinit var mOnBookmarkClickListener: OnBookmarkItemClickListener

    fun setOnBookmarkItemClickListener(mOnBookmarkClickListener: OnBookmarkItemClickListener) {
        this.mOnBookmarkClickListener = mOnBookmarkClickListener
    }

    inner class ProductViewHolder(val binding: ItemViewProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBookmark.setOnClickListener {
                mOnBookmarkClickListener.onBookmarkClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ProductViewHolder(
            ItemViewProductBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = holder as ProductViewHolder
        holder.binding.root.tag = holder

        holder.binding.textUserName.text = products[position].username

        val params: ViewGroup.LayoutParams =
            holder.binding.frameMediaContainer.layoutParams
        val placeholderWidth = getScreenWidth()
        val placeholderHeight = calculatePlaceHolderHeight(
            products[position].largeImageWidth,
            products[position].largeImageHeight
        )
        params.width = placeholderWidth
        params.height = placeholderHeight
        holder.binding.frameMediaContainer.layoutParams = params

        glide.load(products[position].largeImageURL).into(holder.binding.imageThumbnail)

        if (products[position].views > 1) {
            "${products[position].views} ${
                holder.binding.root.context.getString(R.string.views)
            }".also { holder.binding.textViewsCount.text = it }
        } else {
            "${products[position].views} ${
                holder.binding.root.context.getString(R.string.view)
            }".also { holder.binding.textViewsCount.text = it }
        }

        holder.binding.textDescription.text = products[position].tags

        checkIfItemBookmarked(position, holder.binding.imageBookmark)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val holder = holder as ProductViewHolder
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val o = payloads[0] as Bundle
            for (key in o.keySet()) {
                if (key == "isBookmarked") {
                    checkIfItemBookmarked(position, holder.binding.imageBookmark)
                }
            }
        }
    }

    override fun getItemCount(): Int = products.size

    fun setData(newData: List<Product>) {
        val diffResult =
            DiffUtil.calculateDiff(ProductDiffUtilCallBack(newData, products))
        diffResult.dispatchUpdatesTo(this)
        products.clear()
        products.addAll(newData)
    }

    private fun checkIfItemBookmarked(position: Int, imageView: ImageView) {
        if (products[position].isBookmarked) {
            imageView.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else {
            imageView.setImageResource(R.drawable.ic_outline_bookmark_border_24)
        }
    }
}