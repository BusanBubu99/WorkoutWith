package com.bubu.workoutwithclient.firebasechat

data class ChatRoom(
    var matchId: String,
    var messages : String,
    var title: String, //Format is 지역이름 + 종목
    var users : String
)