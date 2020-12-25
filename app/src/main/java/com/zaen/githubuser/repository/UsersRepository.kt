package com.zaen.githubuser.repository

import com.zaen.githubuser.api.RetrofitInstance

class UsersRepository {

    suspend fun searchUsers(usernameQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchUsers(usernameQuery, pageNumber)

    suspend fun getUserDetails(username: String) =
        RetrofitInstance.api.getUserDetails(username)

    suspend fun getFollowersUserData(username: String) =
        RetrofitInstance.api.getFollowersUserData(username)

    suspend fun getFollowingsUserData(username: String) =
        RetrofitInstance.api.getFollowingsUserData(username)
}