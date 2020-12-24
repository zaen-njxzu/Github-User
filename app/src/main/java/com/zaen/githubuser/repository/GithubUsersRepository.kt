package com.zaen.githubuser.repository

import com.zaen.githubuser.api.RetrofitInstance

class GithubUsersRepository {

    suspend fun searchGithubUsers(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchGithubUsers(searchQuery, pageNumber)

    suspend fun getDetailGithubUserByUsername(username: String) =
        RetrofitInstance.api.getUserDetailByUsername(username)

    suspend fun getGithubUserFollowers(username: String) =
        RetrofitInstance.api.getFollowersUserData(username)

    suspend fun getGithubUserFollowing(username: String) =
        RetrofitInstance.api.getFollowingUserData(username)
}