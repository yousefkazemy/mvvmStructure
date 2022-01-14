package com.example.mvvmstructure.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmstructure.data.repository.login.LoginRepository
import com.example.mvvmstructure.utils.Event
import com.example.mvvmstructure.utils.Resource
import com.example.mvvmstructure.utils.Status
import com.example.mvvmstructure.utils.validation.LoginValidation
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginBtnStatus = MutableLiveData<Boolean>()
    val loginBtnStatus: LiveData<Boolean> = _loginBtnStatus

    private val _loginStatus = MutableLiveData<Event<Resource<Boolean>>>()
    val loginStatus: LiveData<Event<Resource<Boolean>>> = _loginStatus

    fun validateFields(email: String, password: String) {
        _loginBtnStatus.value = LoginValidation.validate(email = email, password = password)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(email = email, password = password)
            if (response.status == Status.SUCCESS) {
                _loginStatus.value = Event(Resource.success(true))
            } else {
                _loginStatus.value = Event(Resource.error(response.message!!, null))
            }
        }
    }
}