package com.bubu.workoutwithclient.userinterface.match

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.databinding.MatchJoinScheduleFragmentBinding
import com.bubu.workoutwithclient.databinding.MatchTeamRecyclerBinding
import com.bubu.workoutwithclient.retrofitinterface.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun getVoteInfo(voteId: String): Any? {
    val getVoteObject = UserGetRoomVoteModule(UserGetRoomVoteData(voteId))
    val result = getVoteObject.getApiData()
    if (result is UserGetRoomVoteResponseData) {
        Log.d("voteInfo!", result.toString())
        return result
    } else if (result is UserError) {
        return result
    } else {
        return result
    }
}

suspend fun startVote(matchId: String, voteId: String): Any? {
    val voteObject = UserStartRoomVoteModule(UserStartRoomVoteData(matchId, voteId))
    val result = voteObject.getApiData()
    if (result in 200..299) {
        return result
    } else if (result is UserError) {
        return result
    } else {
        return result
    }
}

class VoteUserAdapter(val mContext: Context) : RecyclerView.Adapter<VoteUserHolder>() {
    var voteUserList = mutableListOf<MatchTeam>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteUserHolder {
        val binding =
            MatchTeamRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteUserHolder(binding, mContext)
    }

    override fun onBindViewHolder(holder: VoteUserHolder, position: Int) {
        val voteUser = voteUserList[position]
        holder.drawPicture(voteUser)
    }

    override fun getItemCount(): Int {
        return voteUserList.size
    }
}

class VoteUserHolder(val binding: MatchTeamRecyclerBinding, val mContext : Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun drawPicture(voteUser: MatchTeam) {
        CoroutineScope(Dispatchers.Main).launch {
            binding.profilePicture.setImageBitmap(voteUser.profilePic)
            binding.textListTitle.text = voteUser.userId
        }
        binding.profilePicture.setOnClickListener {
            val intent = Intent(mContext, MatchUserProfileActivity::class.java)
            //Log.d("bindingIntent", binding.textListTitle.text.toString())
            intent.putExtra("targetId", binding.textListTitle.text.toString())
            mContext.startActivity(intent)
        }
    }
}


class MatchJoinScheduleFragment : Fragment() {

    lateinit var matchRoomActivity: MatchRoomActivity
    lateinit var binding: MatchJoinScheduleFragmentBinding
    lateinit var voteInfo: UserGetRoomVoteResponseData
    lateinit var mContext : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MatchJoinScheduleFragmentBinding.inflate(inflater, container, false)

        val voteId = arguments?.getString("voteId").toString()
        Log.d("voteId", voteId)

        mContext = matchRoomActivity
        var voteUserProfileAdapter = VoteUserAdapter(mContext)
        var voteUserList = mutableListOf<MatchTeam>()

        CoroutineScope(Dispatchers.Default).launch {
            voteInfo = getVoteInfo(voteId) as UserGetRoomVoteResponseData
            voteInfo.userList.forEach {
                val bitmap = withContext(Dispatchers.IO) {
                    downloadProfilePic(it.profilePic)
                }
                voteUserList.add(MatchTeam(it.userId, bitmap))
            }
            CoroutineScope(Dispatchers.Main).launch {
                voteUserProfileAdapter.voteUserList = voteUserList
                binding.recyclerJoinedProfile.adapter = voteUserProfileAdapter
                binding.textJoinContent.text = voteInfo.content
                binding.textJoinDate.text = voteInfo.date
                binding.textJoinTitle.text = voteInfo.voteTitle
                binding.textJoinPerson.text = voteInfo.userList.size.toString() + " 명"

            }
        }
        binding.recyclerJoinedProfile.layoutManager = GridLayoutManager(matchRoomActivity, 5)


        val builder = AlertDialog.Builder(matchRoomActivity)
        with(binding) {
            //recyclerJoinedProfile.adapter = adapter
            //recyclerJoinedProfile.layoutManager = GridLayoutManager(matchRoomActivity, 5)

            btnSubmitJoinSchedule.setOnClickListener {
                //val direction =
                //  JoinScheduleFragmentDirections.actionJoinScheduleFragmentToMatchingTeamFragment()

                CoroutineScope(Dispatchers.Default).launch {
                    Log.d("Inner voteInfo!!", voteInfo.toString())
                    val code = startVote(voteInfo.matchId, voteInfo.voteId)
                    if (code in 200..299) {
                        CoroutineScope(Dispatchers.Main).launch {
                            builder.setMessage("참가 신청이 완료되었습니다!")
                            builder.setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialogInterface, i -> matchRoomActivity?.goBack() })
                            builder.show()
                        }

                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            builder.setMessage("이미 신청 완료한 일정입니다.")
                            builder.setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialogInterface, i -> matchRoomActivity?.goBack() })
                            builder.show()
                        }
                    }
                }
            }
        }
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        matchRoomActivity = context as MatchRoomActivity
    }

    override fun onResume() {
        matchRoomActivity?.setTitle("일정 참여하기")
        super.onResume()
    }
}