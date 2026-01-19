package com.example.fruitties.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "remote_keys")
data class RemoteKeys @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey
    val fruittieId: Long,
    val prevKey: Int?,
    val nextKey: Int?,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
