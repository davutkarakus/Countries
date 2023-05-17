package com.davutkarakus.kotlin_ulkeler_uygulamasi.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.davutkarakus.kotlin_ulkeler_uygulamasi.R
import com.davutkarakus.kotlin_ulkeler_uygulamasi.adapter.CountryAdapter
import com.davutkarakus.kotlin_ulkeler_uygulamasi.model.Country
import com.davutkarakus.kotlin_ulkeler_uygulamasi.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {
    private lateinit var viewModel : FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()
        countryList.layoutManager=LinearLayoutManager(context)
        countryList.adapter=countryAdapter

       /* fragment_button.setOnClickListener {
            val action=FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
        }
        */
        swipeRefreshLayout.setOnRefreshListener {
            countryList.visibility = View.GONE
            countryError.visibility = View.GONE
            countryLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }
        observeLiveData()
    }
    fun observeLiveData(){
        viewModel.countries.observe(viewLifecycleOwner, Observer {
            it?.let {
                countryList.visibility=View.VISIBLE
                countryAdapter.updateCountryList(it)
                countryError.visibility=View.INVISIBLE
                countryLoading.visibility=View.INVISIBLE
            }
        })
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    countryLoading.visibility=View.VISIBLE
                    countryList.visibility=View.GONE
                    countryError.visibility=View.GONE
                }else{
                    countryLoading.visibility=View.GONE
                }

            }
        })
        viewModel.countryError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it){
                    countryError.visibility=View.VISIBLE

                }else{
                    countryError.visibility=View.GONE
                }

            }
        })
    }
}