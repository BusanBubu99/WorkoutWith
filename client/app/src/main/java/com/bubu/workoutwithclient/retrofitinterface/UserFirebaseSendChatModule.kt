package com.bubu.workoutwithclient.retrofitinterface

import com.bubu.workoutwithclient.firebasechat.ChatMessage
import com.bubu.workoutwithclient.firebasechat.ChatVoteMessage
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFirebaseSendChatModule(
    val userId: String,
    val matchId: String,
    val messageString: String
) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val msgRef = database.getReference("rooms/${matchId}/messages")
    fun sendChat() {
        val message = ChatMessage(userId, messageString, "0")
        val msgId = msgRef.push().key!!
        msgRef.child(msgId).setValue(message)
    }
}

class UserFirebaseVoteChatModule(
    val userId: String,
    val matchId: String,
    val voteTitle : String,
    val voteId : String,
    val startTime: String,
    val endTime: String,
    val date: String,
    val content: String
) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val msgRef = database.getReference("rooms/${matchId}/messages")
    fun sendChat() {

        val message = ChatVoteMessage(userId,voteId, voteTitle, startTime, endTime, date, content)

        val msgId = msgRef.push().key!!
        msgRef.child(msgId).setValue(message)
    }
}
