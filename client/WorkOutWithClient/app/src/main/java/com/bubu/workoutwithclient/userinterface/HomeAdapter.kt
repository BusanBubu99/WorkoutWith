package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.databinding.RecyclerHomeBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


class HomeAdapter(context : Context) : RecyclerView.Adapter<HomeAdapter.Holder>() {

    var mcontext = context
    var userMatchList = mutableListOf<UserGetMatchListResponseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = com.bubu.workoutwithclient.databinding.RecyclerHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val matchData = userMatchList.get(position)
        holder.setMatchList(matchData)
    }

    override fun getItemCount(): Int {
        return userMatchList.size
    }

    inner class Holder(val binding: RecyclerHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                //val pos = adapterPosition
                Log.d("테스트",binding.textCity.text.toString())
                CoroutineScope(Dispatchers.Default).launch {
                    val result = getMatchRoom(binding.textMatchId.text.toString())
                    if(result is UserGetMatchRoomResponseData){
                        Log.d("result!@@@",result.toString())
                        CoroutineScope(Dispatchers.Main).launch {
                            val intent = Intent(mcontext, MatchRoomActivity::class.java)
                            intent.putExtra("matchRoom",result as Serializable)
                            mcontext.startActivity(intent)
                        }
                    } else {

                    }
                }
            }
        }

        fun setMatchList(matchData : UserGetMatchListResponseData) {
            binding.textCity.text = matchData.city
            binding.textCounty.text = matchData.county
            binding.textDistrict.text = matchData.district
            binding.textGame.text = matchData.game
            binding.textMatchId.text = matchData.matchId
        }


    }


}

suspend fun getMatchRoom(matchId : String) : Any? {
    val getMatchRoomObject = UserGetMatchRoomModule(UserGetMatchRoomData(matchId))
    val result = getMatchRoomObject.getApiData()
    if(result is UserGetMatchRoomResponseData) {
        Log.d("what!",result.toString())
        return result as UserGetMatchRoomResponseData
    } else if(result is UserError){
        result.message.forEach {
            Log.d("Error",it)
        }
        return result
    } else {
        Log.d("Exception",result.toString())
        return result
    }
}
