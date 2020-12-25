package com.zaen.githubuser.repository

import com.zaen.githubuser.api.RetrofitInstance
import com.zaen.githubuser.db.UserInfoDatabase
import com.zaen.githubuser.models.UserInfo

class UsersRepository(
    val db: UserInfoDatabase
) {

    suspend fun searchUsers(usernameQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchUsers(usernameQuery, pageNumber)

    suspend fun getUserDetails(username: String) =
        RetrofitInstance.api.getUserDetails(username)

    suspend fun getFollowersUserData(username: String) =
        RetrofitInstance.api.getFollowersUserData(username)

    suspend fun getFollowingsUserData(username: String) =
        RetrofitInstance.api.getFollowingsUserData(username)

    suspend fun upsert(userInfo: UserInfo) = db.getUserInfoDao().upsert(userInfo)

    fun getFavoriteUsers() = db.getUserInfoDao().getUsersInfo()

    suspend fun deleteFavoriteUser(userInfo: UserInfo) = db.getUserInfoDao().deletUserInfo(userInfo)

}