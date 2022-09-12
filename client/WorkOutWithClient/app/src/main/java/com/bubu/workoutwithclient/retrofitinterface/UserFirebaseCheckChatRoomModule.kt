package com.bubu.workoutwithclient.retrofitinterface

import android.util.Log
import com.bubu.workoutwithclient.databinding.ActivityMatchRoomBinding
import com.bubu.workoutwithclient.firebasechat.ChatRoom
import com.bubu.workoutwithclient.userinterface.MatchingTeamAdapter
import com.bubu.workoutwithclient.userinterface.updateUserProfilePicture
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserFirebaseCheckChatRoomModule(val matchId: String, val title: String, val profileAdapter : MatchingTeamAdapter,
val binding : ActivityMatchRoomBinding) {
    val database =
        Firebase.database("https://workoutwith-81ab7-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val roomRef = database.getReference("rooms")

    init {
        roomRef.child(matchId).get().addOnSuccessListener {
            if (it.exists()) {
                Log.d("result", "${matchId} 채팅방 이미 존재")
                roomRef.child(matchId + "/users/${userInformation.userId.replace(".", ",")}")
                    .setValue(userInformation.userId)
            } else {
                Log.d("result", "${matchId} 채팅방 존재하지않음")

                val room = ChatRoom(matchId, "", title, "")
                roomRef.child(matchId).setValue(room)
                roomRef.child(matchId + "/users/${userInformation.userId.replace(".", ",")}")
                    .setValue(userInformation.userId)
            }
        }
        roomRef.child(matchId + "/users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("dataCHange","User 입장!!")
                updateUserProfilePicture(profileAdapter,binding)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}