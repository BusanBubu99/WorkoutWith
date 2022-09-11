package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.bubu.workoutwithclient.firebasechat.ChatRoom
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFirebaseCheckChatRoomModule(val matchId : String, val title : String) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val roomRef = database.getReference("rooms")
    init {
        roomRef.child(matchId).get().addOnSuccessListener {
            if(it.exists()) {
                Log.d("result","${matchId} 채팅방 이미 존재")
            } else {
                Log.d("result","${matchId} 채팅방 존재하지않음")
                val room = ChatRoom(matchId,"",title)
                roomRef.child(matchId).setValue(room)
            }
        }
    }
}