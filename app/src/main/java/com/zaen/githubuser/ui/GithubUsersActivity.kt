package com.zaen.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.zaen.githubuser.R
import com.zaen.githubuser.repository.UsersRepository

class GithubUsersActivity : AppCompatActivity() {

    lateinit var usersViewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_users)

        val repository = UsersRepository()
        val githubUsersViewModelProviderFactory = GithubUsersViewModelProviderFactory(application, repository)
        usersViewModel = ViewModelProvider(this, githubUsersViewModelProviderFactory).get(GithubUsersViewModel::class.java)

    }
}
