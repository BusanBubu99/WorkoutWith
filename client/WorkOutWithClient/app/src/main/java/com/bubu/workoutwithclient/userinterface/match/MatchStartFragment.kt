package com.bubu.workoutwithclient.userinterface.match

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bubu.workoutwithclient.databinding.MatchStartFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import com.bubu.workoutwithclient.userinterface.MainScreenActivity
import com.bubu.workoutwithclient.userinterface.gameCodeList
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

suspend fun startMatch(city: String, county: String, district: String, game: String): Any? {
    val startMatchObject = UserStartMatchModule(UserStartMatchData(city, county, district, game))
    val result = startMatchObject.getApiData()
    if (result is UserStartMatchResponseData) {
        return result as UserStartMatchResponseData
    } else if (result is UserError) {
        return result
    } else {
        return result
    }
}


class MatchStartFragment : Fragment() {
    lateinit var mainScreenActivity: MainScreenActivity
    lateinit var binding: MatchStartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var cityCode = ""
        var cityName = ""
        var countyCode = ""
        var countyName = ""
        var districtName = ""
        var gameCode = ""
        binding = MatchStartFragmentBinding.inflate(inflater, container, false)

        var gameAdapter = GameAdapter(mainScreenActivity, R.layout.simple_list_item_1, gameCodeList)
        binding.spinnerGame.adapter = gameAdapter

        binding.spinnerGame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("gameCode",position.toString())
                gameCode = position.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


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
                    binding.btnMatchingStart.isClickable = false
                    val countyList = getCity.getDetailCity(cityCode)
                    if (countyList is List<*>) {
                        countyList as MutableList<UserCityResponseData>
                        var selectRegion = mutableListOf<String>()
                        countyList.forEach {
                            selectRegion.add(it.cityName)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            var regionAdapter =
                                MatchCityAdapter(
                                    mainScreenActivity,
                                    R.layout.simple_list_item_1,
                                    countyList
                                )
                            binding.spinnerCounty.adapter = regionAdapter
                            binding.btnMatchingStart.isClickable = true
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
                    binding.btnMatchingStart.isClickable = false
                    val countyList = getCity.getDetailCity(countyCode)
                    if (countyList is List<*>) {
                        countyList as MutableList<UserCityResponseData>
                        var selectRegion = mutableListOf<String>()
                        countyList.forEach {
                            selectRegion.add(it.cityName)
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            var regionAdapter =
                                MatchCityAdapter(
                                    mainScreenActivity,
                                    R.layout.simple_list_item_1,
                                    countyList
                                )
                            binding.spinnerDistrict.adapter = regionAdapter
                            binding.btnMatchingStart.isClickable = true
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
            binding.btnMatchingStart.isClickable = false
            val cityList = getCity() //as List<UserCityResponseData>
            if (cityList is List<*>) {
                cityList as MutableList<UserCityResponseData>
                var selectRegion = mutableListOf<String>()
                cityList.forEach {
                    selectRegion.add(it.cityName)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    var regionAdapter =
                        MatchCityAdapter(mainScreenActivity, R.layout.simple_list_item_1, cityList)
                    binding.spinnerCity.adapter = regionAdapter
                    binding.btnMatchingStart.isClickable = true
                }
            } else if (cityList is UserError) {

            } else {

            }
        }

        binding.btnBackToHome.setOnClickListener {
            mainScreenActivity?.goBack()
        }

        binding.btnMatchingStart.setOnClickListener {
            val intent = Intent(this.context, MatchRoomActivity::class.java)
            Log.d("Selected Location ", cityName + countyName + districtName)
            CoroutineScope(Dispatchers.Default).launch {
                val matchIdObject = startMatch(cityName, countyName, districtName, gameCode)
                if (matchIdObject is UserStartMatchResponseData) {
                    val matchRoomData = getMatchRoom(matchIdObject.matchId)
                    if (matchRoomData is UserGetMatchRoomResponseData) {
                        CoroutineScope(Dispatchers.Main).launch {
                            intent.putExtra("matchRoom", matchRoomData as Serializable)
                            startActivity(intent)
                        }
                    }
                } else if (matchIdObject is UserError) {

                } else {

                }
            }

        }
        return binding.root
    }

    override fun onResume() {
        mainScreenActivity?.setTitle("매칭 시작하기")
        mainScreenActivity?.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainScreenActivity = context as MainScreenActivity
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.bubu.workoutwithclient.R.menu.match_start_action_bar, menu)
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> mainScreenActivity?.goBack()
        }
        return super.onOptionsItemSelected(item)
    }

}