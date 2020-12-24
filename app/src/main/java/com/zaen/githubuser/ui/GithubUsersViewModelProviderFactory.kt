package com.zaen.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaen.githubuser.repository.GithubUsersRepository

class GithubUsersViewModelProviderFactory(
    val app: Application,
    val githubUsersRepository: GithubUsersRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GithubUsersViewModel(app, githubUsersRepository) as T
    }
}