package com.bubu.workoutwithclient.firebasechat

import com.bubu.workoutwithclient.retrofitinterface.userInformation

data class ChatUser(var id: String = userInformation.userId.replace(".",","),
               var password: String = userInformation.userId.replace(".",","),
               var name : String
)
