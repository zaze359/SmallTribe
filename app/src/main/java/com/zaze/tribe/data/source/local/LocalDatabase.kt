package com.zaze.tribe.data.source.local

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.zaze.tribe.App
import com.zaze.tribe.data.dao.LrcDao
import com.zaze.tribe.data.dao.MusicDao
import com.zaze.tribe.data.dto.LrcInfo
import com.zaze.tribe.data.dto.MusicInfo
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-07-05 - 22:58
 */

@Database(entities = arrayOf(MusicInfo::class, LrcInfo::class), version = 2)
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
    abstract fun getLrcDao(): LrcDao


}