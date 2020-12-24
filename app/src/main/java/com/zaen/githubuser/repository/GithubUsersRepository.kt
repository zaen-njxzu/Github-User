package com.zaen.githubuser.repository

import com.zaen.githubuser.api.RetrofitInstance

class GithubUsersRepository {

    suspend fun searchGithubUsers(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchGithubUsers(searchQuery, pageNumber)
}