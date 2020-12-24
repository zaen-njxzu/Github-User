package com.zaen.githubuser.models

data class SearchGithubUsersResponse(
    val incomplete_results: Boolean,
    val items: MutableList<GithubUserInfo>,
    val total_count: Int
)