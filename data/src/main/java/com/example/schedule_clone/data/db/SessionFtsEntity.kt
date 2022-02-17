package com.example.schedule_clone.data.db

import androidx.room.Entity
import com.example.schedule_clone.model.Session

/**
 * This class represents [Session] data for searching with FTS
 *
 * the [ColumnInfo] name is explicitly declared to allow flexibility for renaming the data class
 * properties without requiring changing the column name.
 */
@Entity(tableName = "sessionFts")
class SessionFtsEntity {
}