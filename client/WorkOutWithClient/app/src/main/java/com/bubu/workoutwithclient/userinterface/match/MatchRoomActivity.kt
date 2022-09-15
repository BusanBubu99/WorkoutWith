package com.bubu.workoutwithclient.userinterface.match

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.MatchMyChatRecyclerBinding
import com.bubu.workoutwithclient.databinding.MatchOpChatRecyclerBinding
import com.bubu.workoutwithclient.databinding.MatchRoomActivityBinding
import com.bubu.workoutwithclient.firebasechat.ChatMessage
import com.bubu.workoutwithclient.firebasechat.ChatVoteMessage
import com.bubu.workoutwithclient.firebasechat.millsecondToDate
import com.bubu.workoutwithclient.retrofitinterface.*
import com.bubu.workoutwithclient.userinterface.gameCodeList
import kotlinx.coroutines.*
import java.net.URL

fun checkChatRoom(matchId: String, title: String, profileAdapter: MatchTeamAdapter, binding: MatchRoomActivityBinding) {
    val checkChatObject = UserFirebaseCheckChatRoomModule(matchId, title,profileAdapter,binding)
}

fun sendMessage(userId: String, matchId: String, messageString: String) {
    val sendMessageObject = UserFirebaseSendChatModule(userId, matchId, messageString)
    sendMessageObject.sendChat()
}

fun createVote(
    userId: String,
    matchId: String,
    voteTitle: String,
    voteId: String,
    startTime: String,
    endTime: String,
    date: String,
    content: String
) {
    val createVoteObject =
        UserFirebaseVoteChatModule(
            userId,
            matchId,
            voteTitle,
            voteId,
            startTime,
            endTime,
            date,
            content
        )
    createVoteObject.sendChat()
}


suspend fun downloadProfilePic(uri: String): Bitmap {
    var url : URL = if(!uri.contains("http://"))
        URL(baseurl + uri)
    else
        URL(uri)
    val stream = url.openStream()
    return BitmapFactory.decodeStream(stream)
}

fun updateUserProfilePicture(
    profileAdapter: MatchTeamAdapter,
    binding: MatchRoomActivityBinding
) {
    var data = mutableListOf<MatchTeam>()
    CoroutineScope(Dispatchers.Default).launch {
        matchRoomData = getMatchRoom(matchRoomData.matchId) as UserGetMatchRoomResponseData
        Log.d("updated!!", matchRoomData.toString())
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("inner!", matchRoomData.toString())
            matchRoomData.userInfo.forEach {
                val bitmap = withContext(Dispatchers.IO) {
                    downloadProfilePic(it.profilePic)
                }
                data.add(MatchTeam(it.userid, bitmap))
            }
            profileAdapter.listMatchingTeamData = data
            binding.recyclerProfile.adapter = profileAdapter
        }
    }
}


lateinit var matchRoomData: UserGetMatchRoomResponseData

class MatchRoomActivity : AppCompatActivity() {
    val binding by lazy { MatchRoomActivityBinding.inflate(layoutInflater) }
    lateinit var list: MutableList<ChatMessage>
    lateinit var adapter: ChatListAdapter

    fun goBack() {
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = mutableListOf<ChatMessage>()
        val intent = intent
        matchRoomData = intent.getSerializableExtra("matchRoom") as UserGetMatchRoomResponseData
        setContentView(binding.root)


        binding.createVoteButton.setOnClickListener {
            goCreateScheduleFragment()
        }


        var profileAdapter = MatchTeamAdapter(this)

        var data = mutableListOf<MatchTeam>()
        CoroutineScope(Dispatchers.Main).launch {
            matchRoomData.userInfo.forEach {
                val bitmap = withContext(Dispatchers.IO) {
                    downloadProfilePic(it.profilePic)
                }
                data.add(MatchTeam(it.userid, bitmap))
            }
            profileAdapter.listMatchingTeamData = data
            binding.recyclerProfile.adapter = profileAdapter
        }

        Log.d("data@@", data.toString())


        binding.recyclerProfile.layoutManager = GridLayoutManager(this, 5)



        adapter = ChatListAdapter(list)
        binding.recyclerMemberChat.adapter = adapter
        binding.recyclerMemberChat.layoutManager = LinearLayoutManager(baseContext)



        binding.btnSendMessage.setOnClickListener {
            if (binding.editSendMessage.text.isNotEmpty()) {
                sendMessage(
                    userInformation.userId,
                    matchRoomData.matchId,
                    binding.editSendMessage.text.toString()
                )
                binding.editSendMessage.setText("")
            }
        }
        Log.d("match!!!!", matchRoomData.toString())
        val title = matchRoomData.city + " " + matchRoomData.county + " " + matchRoomData.district
        checkChatRoom(matchRoomData.matchId, title,profileAdapter,binding)
        val firebaseChatObject = UserFirebaseReceiveChatModule(
            userInformation.userId,
            matchRoomData.matchId,
            list,
            adapter, binding
        )
        firebaseChatObject.receiveChat()
    }

    fun goCreateScheduleFragment() {

        val createScheduleFragment = MatchCreateScheduleFragment()
        val bundle = Bundle()
        bundle.putString("matchId", matchRoomData.matchId)
        createScheduleFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.matchRoomLayout, createScheduleFragment)
        transaction.addToBackStack("test")

        transaction.commit()
    }

    fun goJoinScheduleFragment(msg: ChatMessage) {

        val matchJoinScheduleFragment = MatchJoinScheduleFragment()
        val bundle = Bundle()
        bundle.putString("voteId", (msg as ChatVoteMessage).voteId)
        matchJoinScheduleFragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.matchRoomLayout, matchJoinScheduleFragment)
        transaction.addToBackStack("")

        transaction.commit()
    }

    inner class ChatListAdapter(private val chatList: MutableList<ChatMessage>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {



        override fun getItemViewType(position: Int): Int {
            if (chatList[position].id != userInformation.userId) {
                return 0
            } else {
                return 1
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 0) {
                val binding =
                    MatchOpChatRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HolderOp(binding)
            } else {
                val binding =
                    MatchMyChatRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HolderMy(binding)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val msg = chatList[position]
            if (msg.id != userInformation.userId) {
                (holder as HolderOp).setMsg(msg)
            } else {
                (holder as HolderMy).setMsg(msg)
            }
        }

        override fun getItemCount(): Int {
            return chatList.size
        }

        inner class HolderOp(val binding: MatchOpChatRecyclerBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun setMsg(msg: ChatMessage) {
                if(msg.type == "1"){
                    binding.textMsg.setOnClickListener {
                        Log.d("voteClick",(msg as ChatVoteMessage).voteId)
                        goJoinScheduleFragment(msg)
                    }
                    binding.textMsg.setTextColor(0x00BFFF)
                    binding.textMsg.setTypeface(null,Typeface.BOLD)
                }
                binding.textName.text = msg.id
                binding.textMsg.text = msg.msg
                binding.textDate.text = millsecondToDate(msg.timestamp)//"${msg.timestamp}"
            }
        }

        inner class HolderMy(val binding: MatchMyChatRecyclerBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun setMsg(msg: ChatMessage) {
                if(msg.type == "1"){
                    binding.textMsg.setOnClickListener {
                        Log.d("voteClick",(msg as ChatVoteMessage).voteId)
                        goJoinScheduleFragment(msg)
                    }
                    binding.textMsg.setTextColor(Color.parseColor("#00BFFF"))
                    binding.textMsg.setTypeface(null,Typeface.BOLD)
                }
                binding.textMsg.text = msg.msg
                binding.textDate.text = millsecondToDate(msg.timestamp)//"${msg.timestamp}"
            }
        }
    }
    override fun onResume() {
        setTitle("${matchRoomData.county} ${matchRoomData.district} ${gameCodeList[matchRoomData.game.toInt()]} 매칭방")
        Log.d("restart","resume")
        super.onResume()
    }

}
