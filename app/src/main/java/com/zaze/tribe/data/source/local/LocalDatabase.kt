package com.zaze.tribe.data.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zaze.tribe.App
import com.zaze.tribe.data.dao.LyricDao
import com.zaze.tribe.data.dao.MusicDao
import com.zaze.tribe.data.dto.Lyric
import com.zaze.tribe.data.dto.Music
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:58
 */

@Database(entities = [Music::class, Lyric::class], version = 2)
abstract class LocalDatabase : RoomDatabase() {
    companion object {
        /**
         * version 1 升级到version 2
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                ZLog.i(ZTag.TAG_DEBUG, "MIGRATION_1_2")
            }
        }

        val database by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder<LocalDatabase>(App.INSTANCE,
                    LocalDatabase::class.java, "small_tribe.db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
        }
    }

    abstract fun getMusicDao(): MusicDao
    abstract fun getLrcDao(): LyricDao

}