package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.zaen.githubuser.R
import com.zaen.githubuser.adapters.GithubFollowAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import com.zaen.githubuser.util.FollowStates
import com.zaen.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_github_detail_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat

class GithubDetailUserFragment : Fragment(R.layout.fragment_github_detail_user) {

    lateinit private var githubUsersViewModel: GithubUsersViewModel
    private val args: GithubDetailUserFragmentArgs by navArgs()

    private val TAG = "GithubDetailUserFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        githubUsersViewModel = (activity as GithubUsersActivity).githubUsersViewModel

        observeAndUpdateDetailGithubUser()
        getDetailGithubUser()
        attachTabLayout()
    }

    private fun attachTabLayout() {
        val screensCount = 2
        pager.adapter = GithubFollowAdapter(this, screensCount, args.githubUserInfo.login)
        pager.offscreenPageLimit = screensCount
        pager.currentItem = 0

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            when(position) {
                FollowStates.Followers.ordinal -> tab.text = "Followers"
                FollowStates.Following.ordinal -> tab.text = "Following"
                else -> tab.text = "Undefined"
            }
        }.attach()
    }

    private fun getDetailGithubUser() {
        val githubUserInfo = args.githubUserInfo
        githubUsersViewModel.getDetailGithubUserByUsername(githubUserInfo.login)
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun observeAndUpdateDetailGithubUser() {
        githubUsersViewModel.detailGithubUser.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userInfo ->
                        MainScope().launch(Dispatchers.Main) {
                            tvUsername.text = userInfo.login
                            tvName.text = userInfo.name
                            tvGithubLink.text = userInfo.html_url
                            tvRepos.text = userInfo.public_repos.toString()
                            tvCreatedAt.text = userInfo.created_at.toDateString()
                            Glide.with(this@GithubDetailUserFragment).load(userInfo.avatar_url).into(ivProfileImage)
                        }
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

    fun String.toDateString() : String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        try {
            val output = formatter.format(parser.parse(this))
            return output
        }catch (e: ParseException) {
            e.printStackTrace()
        }
        return this
    }
}