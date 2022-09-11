package com.bubu.workoutwithclient.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bubu.workoutwithclient.R
import com.bubu.workoutwithclient.databinding.ActivityMainBinding
import com.bubu.workoutwithclient.databinding.ActivityMatchRoomBinding
import com.bubu.workoutwithclient.retrofitinterface.*

fun checkChatRoom(matchId: String, title: String) {
    val checkChatObject = UserFirebaseCheckChatRoomModule(matchId, title)
}

fun sendMessage(userId: String, matchId: String, messageString: String) {
    val sendMessageObject = UserFirebaseSendChatModule(userId, matchId, messageString)
    sendMessageObject.sendChat()
}

fun setMessageReceiveListener(matchId: String) {
    val receiveChatObject = UserFirebaseReceiveChatModule(userInformation.userId, matchId)
    receiveChatObject.receiveChat()
}

fun startVote(
    userId: String,
    matchId: String,
    startTime: String,
    endTime: String,
    date: String,
    content: String
) {
    val startVoteObject =
        UserFirebaseVoteChatModule(userId, matchId, startTime, endTime, date, content)
    startVoteObject.sendChat()
}

class MatchRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityMatchRoomBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = getIntent()
        val matchRoomData = intent.getSerializableExtra("matchRoom") as UserGetMatchRoomResponseData
        setContentView(binding.root)

        binding.voteTest.setOnClickListener {
            startVote(userInformation.userId,matchRoomData.matchId,"startTime","endTime","date","자전거타기")
        }
        binding.btnSendMessage.setOnClickListener {
            if (binding.editSendMessage.text.isNotEmpty())
                sendMessage(
                    userInformation.userId,
                    matchRoomData.matchId,
                    binding.editSendMessage.text.toString()
                )
        }
        setMessageReceiveListener(matchRoomData.matchId)
        Log.d("match!!!!", matchRoomData.toString())
        val title = matchRoomData.city + " " + matchRoomData.county + " " + matchRoomData.district
        checkChatRoom(matchRoomData.matchId, title)
    }
}