package com.zaen.githubuser.models

import androidx.room.Entity
import java.io.Serializable

@Entity(
    tableName = "github_user_info"
)
data class GithubUserInfo(
    val avatar_url: String,
    val login: String
) : Serializable