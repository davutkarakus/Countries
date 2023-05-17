package com.davutkarakus.kotlin_ulkeler_uygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.davutkarakus.kotlin_ulkeler_uygulamasi.R
import com.davutkarakus.kotlin_ulkeler_uygulamasi.databinding.FragmentCountryBinding
import com.davutkarakus.kotlin_ulkeler_uygulamasi.model.Country
import com.davutkarakus.kotlin_ulkeler_uygulamasi.util.downloadFromUrl
import com.davutkarakus.kotlin_ulkeler_uygulamasi.util.placeholderProgressBar
import com.davutkarakus.kotlin_ulkeler_uygulamasi.viewmodel.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country.*


class CountryFragment : Fragment() {
    private lateinit var viewModel : CountryViewModel
    private var countryUuid = 0
    private lateinit var dataBinding : FragmentCountryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_country,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            countryUuid=CountryFragmentArgs.fromBundle(it).countryUuid
        }
        viewModel=ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUuid)
        observeLiveData()
    }
    private fun observeLiveData(){
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country->
            country?.let {
                dataBinding.selectedCountry = it
                /*
                countryName.text=it.countryName
                countryCapital.text=it.countryCapital
                countryCurrency.text=it.countryCurrency
                countryLanguage.text=it.countryLanguage
                countryRegion.text=it.countryRegion
                context?.let {
                    countryImage.downloadFromUrl(country.imageUrl, placeholderProgressBar(it))
                }
                 */
            }
        })
    }
}