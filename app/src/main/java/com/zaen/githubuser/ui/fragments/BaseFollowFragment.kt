package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaen.githubuser.R
import com.zaen.githubuser.adapter.UsersAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import kotlinx.android.synthetic.main.fragment_base_follow.*

open class BaseFollowFragment : Fragment(R.layout.fragment_base_follow) {

    protected lateinit var usersViewModel: GithubUsersViewModel
    protected lateinit var followAdapter: UsersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersViewModel = (activity as GithubUsersActivity).usersViewModel
        setupRecycleView()

    }

    private fun setupRecycleView() {
        followAdapter = UsersAdapter()
        rvFollowUser.apply {
            adapter = followAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}