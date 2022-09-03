package com.bubu.apiinterface

import com.google.gson.JsonObject

class UserAuthModule(override val userData: JsonObject) : UserApiInterface {
    override suspend fun getApiData(): Any? {
        TODO("Not yet implemented")
    }
}