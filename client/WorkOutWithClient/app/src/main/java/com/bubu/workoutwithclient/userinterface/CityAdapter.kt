package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.FragmentHomeBinding
import com.bubu.workoutwithclient.databinding.FragmentMatchingStartBinding
import com.bubu.workoutwithclient.retrofitinterface.UserCityResponseData


class CityAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val cities: List<UserCityResponseData>):
    ArrayAdapter<UserCityResponseData>(context, layoutResource, cities) {


    override fun getItemId(position: Int): Long {
        return cities[position].cityCode.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = cities[position].cityName
        return view
    }
}