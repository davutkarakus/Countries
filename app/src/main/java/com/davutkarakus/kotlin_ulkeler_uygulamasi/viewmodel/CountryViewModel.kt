package com.davutkarakus.kotlin_ulkeler_uygulamasi.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davutkarakus.kotlin_ulkeler_uygulamasi.model.Country
import com.davutkarakus.kotlin_ulkeler_uygulamasi.servis.CountryDataBase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application){
    val countryLiveData = MutableLiveData<Country>()

    fun getDataFromRoom(uuid:Int){
        launch {
            val dao = CountryDataBase(getApplication()).countryDao()
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }
    }
}