package com.zaen.githubuser.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "github_user_info"
)
data class GithubUserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val avatar_url: String,
    val login: String,
    val html_url: String
) : Serializable