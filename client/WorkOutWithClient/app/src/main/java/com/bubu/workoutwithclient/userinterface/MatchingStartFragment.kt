package com.bubu.workoutwithclient.userinterface

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bubu.workoutwithclient.databinding.FragmentMatchingStartBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

val getCity = UserGetAddressModule()

suspend fun getCity(): Any? {
    val result = getCity.getApiData()
    if (result is List<*>) {
        result as List<UserCityResponseData>
        return result
    } else if (result is UserError) {
        return result
    } else {
        return result
    }
}

suspend fun startMatch(city: String,county: String,district : String, game : String) : Any? {
    val startMatchObject = UserStartMatchModule(UserStartMatchData(city,county,district,game))
    val result = startMatchObject.getApiData()
    if(result is UserStartMatchResponseData){
        return result as UserStartMatchResponseData
    } else if (result is UserError) {
        return result
    } else {
        return result
    }
}

class MatchingStartFragment : Fragment() {
    lateinit var majorScreen: MajorScreen
    lateinit var binding: FragmentMatchingStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var cityCode = ""
        var cityName = ""
        var countyCode = ""
        var countyName = ""
        var districtName = ""

        binding = FragmentMatchingStartBinding.inflate(inflater, container, false)

        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cityCode =
                    (binding.spinnerCity.getItemAtPosition(position) as UserCityResponseData).cityCode
                cityName =
                    (binding.spinnerCity.getItemAtPosition(position) as UserCityResponseData).cityName
                Log.d("test", "Selected: ${cityCode}, ${cityName}")
                binding.spinnerCounty.isVisible = true
                CoroutineScope(Dispatchers.Default).launch {
                    val countyList = getCity.getDetailCity(cityCode)
                    if (countyList is List<*>) {
                        countyList as MutableList<UserCityResponseData>
                        var selectRegion = mutableListOf<String>()
                        countyList.forEach {
                            selectRegion.add(it.cityName)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            var regionAdapter =
                                CityAdapter(majorScreen, R.layout.simple_list_item_1, countyList)
                            binding.spinnerCounty.adapter = regionAdapter
                        }
                    } else if (countyList is UserError) {

                    } else {

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerCounty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                countyCode =
                    (binding.spinnerCounty.getItemAtPosition(position) as UserCityResponseData).cityCode
                countyName =
                    (binding.spinnerCounty.getItemAtPosition(position) as UserCityResponseData).cityName
                Log.d("countyCode", countyCode)
                Log.d("countyName", countyName)
                binding.spinnerDistrict.isVisible = true
                CoroutineScope(Dispatchers.Default).launch {
                    val countyList = getCity.getDetailCity(countyCode)
                    if (countyList is List<*>) {
                        countyList as MutableList<UserCityResponseData>
                        var selectRegion = mutableListOf<String>()
                        countyList.forEach {
                            selectRegion.add(it.cityName)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            var regionAdapter =
                                CityAdapter(majorScreen, R.layout.simple_list_item_1, countyList)
                            binding.spinnerDistrict.adapter = regionAdapter
                        }
                    } else if (countyList is UserError) {

                    } else {

                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerDistrict.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    districtName =
                        (binding.spinnerDistrict.getItemAtPosition(position) as UserCityResponseData).cityName
                    binding.btnMatchingStart.isVisible = true
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }


        CoroutineScope(Dispatchers.Default).launch {
            val cityList = getCity() //as List<UserCityResponseData>
            if (cityList is List<*>) {
                cityList as MutableList<UserCityResponseData>
                var selectRegion = mutableListOf<String>()
                cityList.forEach {
                    selectRegion.add(it.cityName)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    var regionAdapter =
                        CityAdapter(majorScreen, R.layout.simple_list_item_1, cityList)
                    binding.spinnerCity.adapter = regionAdapter
                }
            } else if (cityList is UserError) {

            } else {

            }
        }

        binding.btnBackToHome.setOnClickListener {
            val direction =
                MatchingStartFragmentDirections.actionMatchingStartFragmentToHomeFragment()
            findNavController().navigate(direction)
        }
        binding.btnMatchingStart.setOnClickListener {
            val intent = Intent(this.context, MatchRoomActivity::class.java)
            Log.d("Selected Location ", cityName + countyName + districtName)
            //Start the matching
            //val test = "This is Parameter"
            CoroutineScope(Dispatchers.Default).launch {
                val matchIdObject = startMatch(cityName,countyName,districtName,"30")
                if(matchIdObject is UserStartMatchResponseData){
                    val matchRoomData = getMatchRoom(matchIdObject.matchId)
                    if(matchRoomData is UserGetMatchRoomResponseData) {
                        CoroutineScope(Dispatchers.Main).launch {
                            //val bundle = bundleOf("matchId" to matchIdObject.matchId)
                            //setFragmentResult("request", bundle)
                            intent.putExtra("matchRoom", matchRoomData as Serializable)
                            startActivity(intent)
                        }
                    }
                } else if(matchIdObject is UserError) {

                } else {

                }
            }

        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

}