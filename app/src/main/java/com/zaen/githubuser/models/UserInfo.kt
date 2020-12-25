package com.zaen.githubuser.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "user_info"
)
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("avatar_url")
    val profile_image_url: String,
    @SerializedName("login")
    val username: String,
    @SerializedName("html_url")
    val user_github_url: String
) : Serializable