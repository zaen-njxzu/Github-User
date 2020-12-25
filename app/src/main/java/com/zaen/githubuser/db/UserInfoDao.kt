package com.zaen.githubuser.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zaen.githubuser.models.UserInfo

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(userInfo: UserInfo): Long

    @Query("SELECT * FROM user_info")
    fun getUsersInfo() : LiveData<List<UserInfo>>

    @Delete
    suspend fun deletUserInfo(userInfo: UserInfo)

}