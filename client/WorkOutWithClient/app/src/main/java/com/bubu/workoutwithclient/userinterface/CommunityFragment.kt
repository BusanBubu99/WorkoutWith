package com.bubu.workoutwithclient.userinterface

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.databinding.FragmentCommunityBinding
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.retrofitinterface.UserGetCommunityListModule
import com.bubu.workoutwithclient.retrofitinterface.UserGetCommunityListResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable


suspend fun getCommunityList() : Any?{
    val getCommunityObject = UserGetCommunityListModule()
    val result = getCommunityObject.getApiData()
    if(result is List<*>){
        Log.d("result!",result.toString())
        return result
    } else if(result is UserError){
        return result
    } else{
        return result
    }
}



data class Community(var title: String, var content: String, var editor: String, var editTime: String, val picture : Bitmap,val uri : String)
    : Serializable


class  CommunityFragment : Fragment() {

    lateinit var majorScreen: MajorScreen
    lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        //val data : MutableList<Community> = loadCommunityData()
        //var adapter = CommunityAdapter()
        //adapter.listCommunityData = data
        //with(binding) {
            //RecyclerCommunity.adapter = adapter
            //RecyclerCommunity.layoutManager = LinearLayoutManager(majorScreen)
        //}
        binding.btnGoPost.setOnClickListener {
            val direction = CommunityFragmentDirections.actionCommunityFragmentToPostNewFragment()
            findNavController().navigate(direction)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var data : MutableList<Community> = mutableListOf<Community>()
        val communityAdapter = CommunityAdapter()
        CoroutineScope(Dispatchers.Default).launch {
            val communityData = getCommunityList() as List<UserGetCommunityListResponseData>
            communityData.forEach {
                val bitmap = withContext(Dispatchers.IO){
                    downloadProfilePic(it.picture)
                }
                data.add(Community(it.title,it.contents,it.userId,it.timestamp,bitmap,it.picture))
            }
            communityAdapter.listCommunityData = data
            CoroutineScope(Dispatchers.Main).launch {
                binding.RecyclerCommunity.adapter = communityAdapter
                binding.RecyclerCommunity.layoutManager = LinearLayoutManager(majorScreen)
            }
        }

        //val data1 = loadCommunityData()

        //communityAdapter.listCommunityData = data1
        //binding.RecyclerCommunity.adapter = communityAdapter
        //binding.RecyclerCommunity.layoutManager = LinearLayoutManager(this.activity)
        val intent = Intent(this.context, DetailActivity::class.java)
        communityAdapter.setOnItemClickListner(object: CommunityAdapter.OnItemClickListner{
            override fun onItemClick(view: View, position: Int) {
                //intent.putExtra("test",data[position] as Serializable)
                intent.putExtra("pictureUri",data[position].uri)
                intent.putExtra("title", data[position].title)
                intent.putExtra("content", data[position].content)
                intent.putExtra("editor", data[position].editor)
                intent.putExtra("editTime", data[position].editTime)
                startActivity(intent)
            }
        })

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        majorScreen = context as MajorScreen
    }

    /*fun loadCommunityData() : MutableList<Community> {

        val data : MutableList<Community> = mutableListOf()
        for(no in 1..100) {
            val title = "제목 ${no}"
            val content = "내용 ${no}"
            val editor = "작성자 ${no}"
            val date = System.currentTimeMillis()

            var community = Community(title, content, editor, date)

            data.add(community)
        }
        return data
    }*/
}

