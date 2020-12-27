package com.zaen.githubuser.consumerapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val id: Int? = null,
    val profile_image_url: String,
    val username: String,
    val user_github_url: String
) : Parcelable