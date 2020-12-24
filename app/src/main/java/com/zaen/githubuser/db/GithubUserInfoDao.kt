package com.zaen.githubuser.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zaen.githubuser.models.GithubUserInfo

@Dao
interface GithubUserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(githubUserInfo: GithubUserInfo): Long

    @Query("SELECT * FROM github_user_info")
    fun getAllGithubUserInfo() : LiveData<List<GithubUserInfo>>

    @Delete
    suspend fun deleteGithubUserInfo(githubUserInfo: GithubUserInfo)

}