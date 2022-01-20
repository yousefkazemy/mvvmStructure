package com.example.mvvmstructure.ui.search

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.repository.search.SearchRepository
import com.example.mvvmstructure.utils.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchViewModel @ViewModelInject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products

    private val _recyclerPosition = MutableLiveData<Int>()
    val recyclerPosition: LiveData<Int> = _recyclerPosition

    private val _searchField = MutableLiveData<String>()
    val searchField: LiveData<String> = _searchField

    fun setSearchField(text: String) {
        _searchField.value = text
    }

    fun setRecyclerPosition(position: Int) {
        _recyclerPosition.value = position
    }

    private val compositeDisposable = CompositeDisposable()

    fun searchForProduct(searchObservable: Observable<String>) {
        compositeDisposable.add(
            searchObservable.debounce(400, TimeUnit.MILLISECONDS)
                .doOnNext {
                    if (it.isEmpty()) {
                        _products.postValue(Resource.success(emptyList()))
                    } else {
                        _products.postValue(Resource.loading(null))
                    }
                }
                .filter {
                    it.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapSingle {
                    repository.searchForProducts(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _products.postValue(Resource.success(it))
                    }, {
                        _products.postValue(Resource.error("Unknown error occurred", null))
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}