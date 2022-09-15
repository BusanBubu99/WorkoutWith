package com.bubu.workoutwithclient.userinterface.match

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bubu.workoutwithclient.retrofitinterface.UserCityResponseData


class MatchCityAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val cities: List<UserCityResponseData>):
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