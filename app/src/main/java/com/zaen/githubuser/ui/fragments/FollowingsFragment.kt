package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.zaen.githubuser.util.Constants
import com.zaen.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_base_follow.*

class FollowingsFragment : BaseFollowFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAndUpdateFollowingUser()

        arguments?.takeIf { it.containsKey(Constants.FOLLOW_ARG_OBJECT_USERNAME) }?.apply {
            val username = getString(Constants.FOLLOW_ARG_OBJECT_USERNAME)

            username?.let {
                usersViewModel.getFollowingsUserData(it)
            }
        }
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun observeAndUpdateFollowingUser() {
        usersViewModel.followingsUserData.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { usersResponse ->
                        followAdapter.differ.submitList(usersResponse.toList())
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
}