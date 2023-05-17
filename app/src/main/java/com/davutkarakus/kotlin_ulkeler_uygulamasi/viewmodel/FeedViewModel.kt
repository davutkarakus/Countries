package com.davutkarakus.kotlin_ulkeler_uygulamasi.viewmodel

import android.app.Application
import android.app.SyncNotedAppOp
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davutkarakus.kotlin_ulkeler_uygulamasi.model.Country
import com.davutkarakus.kotlin_ulkeler_uygulamasi.servis.CountryAPIService
import com.davutkarakus.kotlin_ulkeler_uygulamasi.servis.CountryDataBase
import com.davutkarakus.kotlin_ulkeler_uygulamasi.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedViewModel(application: Application):BaseViewModel(application) {
    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L
    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()
    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQLite()
        }else {
            getDataFromAPI()
        }
    }
    fun refreshFromAPI() {
        getDataFromAPI()
    }
    private fun getDataFromSQLite() {
        countryLoading.value = true
        launch {
            val countries = CountryDataBase(getApplication()).countryDao().getAllCountries()
            showCounteries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
        }
    }
    private fun getDataFromAPI(){
        countryLoading.value = true
        disposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }
    private fun showCounteries(countryList:List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }
    private fun storeInSQLite(list:List<Country>) {
        launch {
            val dao = CountryDataBase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray()) // -> list -> individual
            var i = 0
            while (i < list.size){
                list[i].uuid = listLong[i].toInt()
                i++
            }
            showCounteries(list)
        }
        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}