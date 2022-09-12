package com.bubu.workoutwithclient.firebasechat

class ChatVoteMessage : ChatMessage {
    var startTime : String = ""
    var endTime : String = ""
    var date : String = ""
    var content : String = ""
    var voteId : String = ""

    constructor()


    constructor(id: String,voteId : String,voteTitle : String,startTime:String, endTime: String,date : String,content:String,type : String = "1",) { // creator = 방 생성자
        super.id = id
        this.voteId = voteId
        this.startTime = startTime
        this.endTime = endTime
        this.date = date
        this.content = content
        super.msg = voteTitle
        super.type = type
        super.timestamp = System.currentTimeMillis()
    }
}