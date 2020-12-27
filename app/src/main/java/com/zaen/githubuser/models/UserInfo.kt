package com.zaen.githubuser.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName = "user_info"
)
@Parcelize
data class UserInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @SerializedName("avatar_url")
    val profile_image_url: String,
    @SerializedName("login")
    val username: String,
    @SerializedName("html_url")
    val user_github_url: String
) : Serializable, Parcelable