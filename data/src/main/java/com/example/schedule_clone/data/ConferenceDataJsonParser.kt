package com.example.schedule_clone.data

import com.example.schedule_clone.data.session.json.SessionTemp
import com.example.schedule_clone.model.ConferenceData
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import java.io.InputStream

object ConferenceDataJsonParser {

    @Throws(JsonIOException::class, JsonSyntaxException::class)
    fun parseConferenceData(unprocessedSessionData: InputStream): ConferenceData {
        val jsonReader = com.google.gson.stream.JsonReader(unprocessedSessionData.reader())

        val gson = GsonBuilder()
            .registerTypeAdapter(SessionTemp::class.java, )
    }

}