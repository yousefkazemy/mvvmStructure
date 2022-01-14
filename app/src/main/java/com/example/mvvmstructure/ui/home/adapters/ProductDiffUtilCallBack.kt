package com.example.mvvmstructure.ui.home.adapters

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.mvvmstructure.data.model.Product

class ProductDiffUtilCallBack(
    private val newList: List<Product>,
    private val oldList: List<Product>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].id == oldList[oldItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        newList[newItemPosition].isBookmarked == oldList[oldItemPosition].isBookmarked

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newModel = newList[newItemPosition]
        val oldModel = oldList[oldItemPosition]

        val diff = Bundle()

        if (newModel.isBookmarked != oldModel.isBookmarked) {
            diff.putBoolean("isBookmarked", newModel.isBookmarked)
        }

        return if (diff.size() == 0) {
            null
        } else diff
    }
}