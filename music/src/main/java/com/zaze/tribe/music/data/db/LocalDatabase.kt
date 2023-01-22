package com.zaze.tribe.music.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zaze.tribe.common.BaseApplication
import com.zaze.tribe.music.data.dto.Lyric
import com.zaze.tribe.music.data.dto.Music
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

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        val database: LocalDatabase
            get() = INSTANCE ?: synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder<LocalDatabase>(
                    BaseApplication.INSTANCE,
                    LocalDatabase::class.java, "small_tribe.db"
                ).addMigrations(MIGRATION_1_2).build().also {
                    INSTANCE = it
                }
            }
    }

    abstract fun getMusicDao(): MusicDao
    abstract fun getLrcDao(): LyricDao

}