package com.zaen.githubuser.consumerapp.helper

import android.database.Cursor
import com.zaen.githubuser.consumerapp.db.util.DatabaseContract
import com.zaen.githubuser.consumerapp.models.UserInfo

object MappingHelper {

    fun mapCursorToUsersInfo(usersCursor: Cursor?): ArrayList<UserInfo> {
        val notesList = ArrayList<UserInfo>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.ID))
                val profileImageUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.PROFILE_IMAGE_URL))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.USERNAME))
                val userGithubUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.USER_GITHUB_URL))
                notesList.add(UserInfo(
                    id,
                    profileImageUrl,
                    username,
                    userGithubUrl
                ))
            }
        }
        return notesList
    }

    fun mapCursorToUserInfo(notesCursor: Cursor?): UserInfo {
        lateinit var userInfo: UserInfo
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.ID))
            val profileImageUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.PROFILE_IMAGE_URL))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.USERNAME))
            val userGithubUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserInfoColumns.USER_GITHUB_URL))
            userInfo = UserInfo(
                id,
                profileImageUrl,
                username,
                userGithubUrl
            )
        }
        return userInfo
    }
}