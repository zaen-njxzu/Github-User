package com.zaen.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zaen.githubuser.models.GithubUserInfo

@Database(
    entities = [GithubUserInfo::class],
    version = 1
)
abstract class GithubUserInfoDatabase : RoomDatabase() {

    abstract fun getGithubUserInfoDao() : GithubUserInfoDao

    companion object {
        @Volatile
        private var instance: GithubUserInfoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GithubUserInfoDatabase::class.java,
                "github_user_info_db.db"
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}