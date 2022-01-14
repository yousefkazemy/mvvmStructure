package com.example.mvvmstructure.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmstructure.R
import com.example.mvvmstructure.data.local.ProductDao
import com.example.mvvmstructure.data.local.ProductsDatabase
import com.example.mvvmstructure.data.remote.ApiModule
import com.example.mvvmstructure.data.remote.MovieApiInterface
import com.example.mvvmstructure.data.repository.login.LoginRepository
import com.example.mvvmstructure.data.repository.login.LoginRepositoryImpl
import com.example.mvvmstructure.data.repository.product.ProductRepository
import com.example.mvvmstructure.data.repository.product.ProductRepositoryImpl
import com.example.mvvmstructure.utils.Constants.DATABASE_NAME
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module()
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = ApiModule.createRetrofit()

    @Singleton
    @Provides
    fun provideMovieApiService(retrofit: Retrofit): MovieApiInterface =
        ApiModule.createMovieApiService(retrofit)

    @Singleton
    @Provides
    fun provideProductsDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ProductsDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideProductDao(
        database: ProductsDatabase
    ) = database.productDao()

    @Singleton
    @Provides
    fun provideProductRepositoryImpl(
        movieApiInterface: MovieApiInterface,
        productDao: ProductDao,
    ) = ProductRepositoryImpl(movieApiInterface, productDao) as ProductRepository

    @Singleton
    @Provides
    fun provideLoginRepositoryImpl(
        movieApiInterface: MovieApiInterface
    ) = LoginRepositoryImpl(movieApiInterface) as LoginRepository

    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.placeholdergradient)
            .error(R.drawable.placeholdergradient)
    )

    @Provides
    @Singleton
    fun provideExoPlayerInstance(
        @ApplicationContext context: Context
    ) = ExoPlayerFactory.newSimpleInstance(
        context,
        DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
    )
}