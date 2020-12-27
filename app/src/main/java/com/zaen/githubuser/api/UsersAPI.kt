package com.zaen.githubuser.api

import com.zaen.githubuser.BuildConfig
import com.zaen.githubuser.models.UserDetailsResponse
import com.zaen.githubuser.models.FollowUserResponse
import com.zaen.githubuser.models.SearchUsersResponse
import com.zaen.githubuser.util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersAPI {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q")
        usernameQuery: String,
        @Query("page")
        pageNumber: Int,
        @Query("per_page")
        resultPerPage: Int = QUERY_PAGE_SIZE,
        @Header("Authorization")
        token: String = BuildConfig.GITHUB_API_KEY
    ) : Response<SearchUsersResponse>

    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username")
        username: String,
        @Header("Authorization")
        token: String = BuildConfig.GITHUB_API_KEY
    ) : Response<UserDetailsResponse>

    @GET("users/{username}/followers")
    suspend fun getFollowersUserData(
        @Path("username")
        username: String,
        @Header("Authorization")
        token: String = BuildConfig.GITHUB_API_KEY
    ) : Response<FollowUserResponse>

    @GET("users/{username}/following")
    suspend fun getFollowingsUserData(
        @Path("username")
        username: String,
        @Header("Authorization")
        token: String = BuildConfig.GITHUB_API_KEY
    ) : Response<FollowUserResponse>

}