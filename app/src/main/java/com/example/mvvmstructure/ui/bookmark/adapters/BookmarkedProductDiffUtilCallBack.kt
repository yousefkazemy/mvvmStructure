package com.example.mvvmstructure.ui.bookmark.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.mvvmstructure.data.model.ProductEntity

class BookmarkedProductDiffUtilCallBack(
    private val newList: List<ProductEntity>,
    private val oldList: List<ProductEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id
}