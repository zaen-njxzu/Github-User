package com.zaen.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zaen.githubuser.R
import com.zaen.githubuser.repository.GithubUsersRepository
import com.zaen.githubuser.util.Resource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GithubUsersActivity : AppCompatActivity() {

    lateinit var githubUsersViewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = GithubUsersRepository()
        val githubUsersViewModelProviderFactory = GithubUsersViewModelProviderFactory(application, repository)
        githubUsersViewModel = ViewModelProvider(this, githubUsersViewModelProviderFactory).get(GithubUsersViewModel::class.java)

    }
}
