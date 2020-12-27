package com.zaen.githubuser.consumerapp.db.util

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.zaen.githubuser.provider"
    const val SCHEME = "content"

    class UserInfoColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "user_info"
            const val ID = "id"
            const val PROFILE_IMAGE_URL = "profile_image_url"
            const val USERNAME = "username"
            const val USER_GITHUB_URL = "user_github_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}