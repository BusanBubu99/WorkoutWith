package com.bubu.workoutwithclient.userinterface.match

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bubu.workoutwithclient.retrofitinterface.UserCityResponseData

class GameAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val gameCodes: List<String>
) :
    ArrayAdapter<String>(context, layoutResource, gameCodes) {

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = gameCodes[position]
        return view
    }

}