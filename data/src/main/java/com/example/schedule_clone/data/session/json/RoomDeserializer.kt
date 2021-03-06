package com.example.schedule_clone.data.session.json

import com.example.schedule_clone.model.Room
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Deserializer for [Room]s.
 */
class RoomDeserializer : JsonDeserializer<Room> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Room {
        val obj = json?.asJsonObject!!
        return Room(
            id = obj.get("id").asString,
            name = obj.get("name").asString
        )
    }
}
