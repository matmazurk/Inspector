package com.mat.inspector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class ChartsViewModel(
    private val repository: RemoteRepository
) : ViewModel() {

    private val _chartsLoaded: MutableLiveData<Boolean> = MutableLiveData()
    val chartsLoaded: LiveData<Boolean> get() = _chartsLoaded

    fun loadCharts(ip: InetAddress, port: Int) = viewModelScope.launch(Dispatchers.IO) {
        _chartsLoaded.postValue(repository.loadCharts(ip, port))
    }



}