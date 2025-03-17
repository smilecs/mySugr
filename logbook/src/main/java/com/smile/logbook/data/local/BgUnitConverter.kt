package com.smile.logbook.data.local

import androidx.room.TypeConverter
import com.smile.logbook.domain.BgUnit
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class BgUnitConverter {
    private val json = Json {
        serializersModule = SerializersModule {
            polymorphic(BgUnit::class) {
                subclass(BgUnit.MmoL::class, BgUnit.MmoL.serializer())
                subclass(BgUnit.Mg::class, BgUnit.Mg.serializer())
            }
        }
    }

    @TypeConverter
    fun fromBgUnit(bgUnit: BgUnit): String {
        return json.encodeToString(bgUnit)
    }

    @TypeConverter
    fun toBgUnit(jsonString: String): BgUnit {
        return json.decodeFromString(jsonString)
    }
}
