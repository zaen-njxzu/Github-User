package com.zaen.githubuser.models

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    val incomplete_results: Boolean,
    @SerializedName("items")
    val users_info: MutableList<UserInfo>,
    val total_count: Int
)