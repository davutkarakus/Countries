package com.davutkarakus.kotlin_ulkeler_uygulamasi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.davutkarakus.kotlin_ulkeler_uygulamasi.R
import com.davutkarakus.kotlin_ulkeler_uygulamasi.databinding.ItemCountryBinding
import com.davutkarakus.kotlin_ulkeler_uygulamasi.model.Country
import com.davutkarakus.kotlin_ulkeler_uygulamasi.util.downloadFromUrl
import com.davutkarakus.kotlin_ulkeler_uygulamasi.util.placeholderProgressBar
import com.davutkarakus.kotlin_ulkeler_uygulamasi.view.CountryFragmentDirections
import com.davutkarakus.kotlin_ulkeler_uygulamasi.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter(val countryList:ArrayList<Country>):RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(),CountryClickListener {
    class CountryViewHolder(var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_country,parent,false)
        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.view.country = countryList[position]
        holder.view.listener = this
        /*
        holder.itemView.name.text=countryList[position].countryName
        holder.itemView.region.text=countryList[position].countryRegion
        holder.itemView.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid)
            Navigation.findNavController(it).navigate(action)
        }
        holder.itemView.imageView.downloadFromUrl(countryList[position].imageUrl,
            placeholderProgressBar(holder.itemView.context))

         */
    }

    override fun getItemCount(): Int {
        return countryList.size
    }
    fun updateCountryList(newCountryList:List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uuid = v.countryUuidText.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid)
        Navigation.findNavController(v).navigate(action)
    }

}