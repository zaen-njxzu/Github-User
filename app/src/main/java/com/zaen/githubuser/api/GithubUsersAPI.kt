package com.zaen.githubuser.api

import com.zaen.githubuser.BuildConfig
import com.zaen.githubuser.models.SearchGithubUsersResponse
import com.zaen.githubuser.util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubUsersAPI {

    @GET("search/users")
    suspend fun searchGithubUsers(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int,
        @Query("per_page")
        resultPerPage: Int = QUERY_PAGE_SIZE,
        @Header("Authorization")
        token: String = BuildConfig.GITHUB_API_KEY
    ) : Response<SearchGithubUsersResponse>
}