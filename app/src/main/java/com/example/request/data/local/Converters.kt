package com.example.request.data.local

import androidx.room.TypeConverter
import com.example.request.domain.model.LeadStatus

class Converters {
    @TypeConverter
    fun fromLeadStatus(status: LeadStatus): String = status.name

    @TypeConverter
    fun toLeadStatus(value: String): LeadStatus = LeadStatus.valueOf(value)
}
