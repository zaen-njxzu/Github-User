package com.zaen.githubuser.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zaen.githubuser.ui.fragments.BaseGithubFollowFragment
import com.zaen.githubuser.ui.fragments.GithubFollowersFragment
import com.zaen.githubuser.ui.fragments.GithubFollowingsFragment
import com.zaen.githubuser.util.Constants.Companion.FOLLOW_ARG_OBJECT_USERNAME
import com.zaen.githubuser.util.FollowStates

class GithubFollowAdapter(fragment: Fragment, private val showItemCount: Int, private val username: String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return showItemCount
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = BaseGithubFollowFragment()

        when(position) {
            FollowStates.Followers.ordinal -> fragment = GithubFollowersFragment()
            FollowStates.Following.ordinal -> fragment = GithubFollowingsFragment()
        }

        fragment.arguments = Bundle().apply {
            putString(FOLLOW_ARG_OBJECT_USERNAME, username)
        }

        return fragment
    }
}