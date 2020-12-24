package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaen.githubuser.R
import com.zaen.githubuser.adapters.GithubUsersAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import kotlinx.android.synthetic.main.fragment_github_follow.*

open class BaseGithubFollowFragment : Fragment(R.layout.fragment_github_follow) {

    lateinit protected var githubUsersViewModel: GithubUsersViewModel
    lateinit protected var followAdapter: GithubUsersAdapter

    private val TAG = "GithubFollowFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        githubUsersViewModel = (activity as GithubUsersActivity).githubUsersViewModel
        setupRecycleView()

    }

    private fun setupRecycleView() {
        followAdapter = GithubUsersAdapter()
        rvFollowUser.apply {
            adapter = followAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}