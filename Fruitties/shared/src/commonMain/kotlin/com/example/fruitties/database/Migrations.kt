package com.example.fruitties.database

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE IF NOT EXISTS `remote_keys` (`fruittieId` INTEGER NOT NULL, `prevKey` INTEGER, `nextKey` INTEGER, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`fruittieId`))",
        )

        connection.execSQL("CREATE TABLE IF NOT EXISTS `Fruittie_new` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `fullName` TEXT NOT NULL, `calories` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("INSERT INTO `Fruittie_new` (`id`, `name`, `fullName`, `calories`) SELECT `id`, `name`, `fullName`, `calories` FROM `Fruittie`")
        connection.execSQL("DROP TABLE `Fruittie`")
        connection.execSQL("ALTER TABLE `Fruittie_new` RENAME TO `Fruittie`")
    }
}
