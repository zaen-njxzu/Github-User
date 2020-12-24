package com.zaen.githubuser.models

data class DetailGithubUserResponse(
    val avatar_url: String,
    val created_at: String,
    val followers: Int,
    val following: Int,
    val html_url: String,
    val login: String,
    val name: String,
    val public_repos: Int
)