package com.proteinium.proteiniumdietapp.data.api

import com.google.gson.JsonObject


interface ApiHelper {

    suspend fun getUsers(): List<JsonObject>
}