package com.zaen.githubuser.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zaen.githubuser.ui.fragments.BaseFollowFragment
import com.zaen.githubuser.ui.fragments.FollowersFragment
import com.zaen.githubuser.ui.fragments.FollowingsFragment
import com.zaen.githubuser.util.Constants.Companion.FOLLOW_ARG_OBJECT_USERNAME
import com.zaen.githubuser.util.FollowStates

class FollowPagerAdapter(fragment: Fragment, private val showItemCount: Int, private val username: String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return showItemCount
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = BaseFollowFragment()

        when(position) {
            FollowStates.Followers.ordinal -> fragment = FollowersFragment()
            FollowStates.Following.ordinal -> fragment = FollowingsFragment()
        }

        fragment.arguments = Bundle().apply {
            putString(FOLLOW_ARG_OBJECT_USERNAME, username)
        }

        return fragment
    }
}