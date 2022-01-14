package com.example.mvvmstructure.di

import com.bumptech.glide.RequestManager
import com.example.mvvmstructure.ui.bookmark.adapters.BookmarkedProductAdapter
import com.example.mvvmstructure.ui.home.adapters.ProductAdapter
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
class FragmentModule {

    fun provideProductAdapter(glide: RequestManager): ProductAdapter = ProductAdapter(glide)

    fun provideBookmarkedProductAdapter(glide: RequestManager): BookmarkedProductAdapter =
        BookmarkedProductAdapter(glide)
}