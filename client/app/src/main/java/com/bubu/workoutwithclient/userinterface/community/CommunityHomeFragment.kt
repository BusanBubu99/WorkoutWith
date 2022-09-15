package com.bubu.workoutwithclient.userinterface.community

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.CommunityHomeFragmentBinding
import com.bubu.workoutwithclient.retrofitinterface.UserError
import com.bubu.workoutwithclient.retrofitinterface.UserGetCommunityListModule
import com.bubu.workoutwithclient.retrofitinterface.UserGetCommunityListResponseData
import com.bubu.workoutwithclient.userinterface.MainScreenActivity
import com.bubu.workoutwithclient.userinterface.match.MatchHomeFragmentDirections
import com.bubu.workoutwithclient.userinterface.match.downloadProfilePic
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



data class Community(var title: String, var content: String, var editor: String, var editTime: String, val picture : Bitmap?,val pictureUri : String?)
    : Serializable


class  CommunityHomeFragment : Fragment() {

    lateinit var mainScreenActivity: MainScreenActivity
    lateinit var binding : CommunityHomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = CommunityHomeFragmentBinding.inflate(inflater, container, false)
        Log.d("PosN","OnCreateView")
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var data : MutableList<Community> = mutableListOf<Community>()
        val communityAdapter = CommunityAdapter()
        updateCommunityList(binding,mainScreenActivity, communityAdapter, data)
        Log.d("PosN","OnViewCreated")
        val intent = Intent(this.context, CommunityViewPostDetailActivity::class.java)
        communityAdapter.setOnItemClickListner(object: CommunityAdapter.OnItemClickListner {
            override fun onItemClick(view: View, position: Int) {
                if(data[position].pictureUri != null)
                    intent.putExtra("pictureUri",data[position].pictureUri)
                else
                    intent.putExtra("pictureUri", "null")
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
        mainScreenActivity = context as MainScreenActivity
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.common_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_common_add_start) {
            val direction = CommunityHomeFragmentDirections.actionCommunityHomeFragmentToCommunityNewPostFragment()
            findNavController().navigate(direction)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        mainScreenActivity?.setTitle("커뮤니티 게시글")
        mainScreenActivity?.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        super.onResume()
    }
}

fun updateCommunityList(binding : CommunityHomeFragmentBinding, mainScreenActivity : MainScreenActivity, communityAdapter : CommunityAdapter,
                        data : MutableList<Community>) {
    CoroutineScope(Dispatchers.Default).launch {
        data.clear()
        val communityData = getCommunityList() as List<UserGetCommunityListResponseData>
        communityData.forEach {
            val bitmap : Bitmap
            if(it.picture != null) {
                bitmap = withContext(Dispatchers.IO) {
                    downloadProfilePic(it.picture)
                }
                data.add(Community(it.title,it.contents,it.userId,it.timestamp,bitmap,it.picture))
                communityAdapter.listCommunityData = data
                Log.d("PosN","updated1")
            } else {
                data.add(Community(it.title,it.contents,it.userId,it.timestamp,null,it.picture))
                communityAdapter.listCommunityData = data
                Log.d("PosN","updated2")
            }
        }
        communityAdapter.listCommunityData = data
        CoroutineScope(Dispatchers.Main).launch {
            binding.RecyclerCommunity.adapter = communityAdapter
            binding.RecyclerCommunity.layoutManager = LinearLayoutManager(mainScreenActivity)
            Log.d("PosN","updated3")
        }
    }
}

