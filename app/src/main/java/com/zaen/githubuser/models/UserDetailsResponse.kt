package com.zaen.githubuser.models

import com.google.gson.annotations.SerializedName

data class UserDetailsResponse(
    @SerializedName("avatar_url")
    val profile_image_url: String,
    val created_at: String,
    val followers: Int,
    val following: Int,
    @SerializedName("html_url")
    val user_gihub_url: String,
    @SerializedName("login")
    val username: String,
    val name: String,
    val public_repos: Int
)