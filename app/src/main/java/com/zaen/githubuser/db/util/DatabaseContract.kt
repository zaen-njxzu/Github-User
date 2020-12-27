package com.zaen.githubuser.db.util

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.zaen.githubuser.provider"
    const val SCHEME = "content"

    class UserInfoColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "user_info"
            const val ID = "id"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}