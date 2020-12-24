package com.zaen.githubuser.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zaen.githubuser.R
import com.zaen.githubuser.adapters.GithubUsersAdapter
import com.zaen.githubuser.ui.GithubUsersActivity
import com.zaen.githubuser.ui.GithubUsersViewModel
import com.zaen.githubuser.util.Constants.Companion.QUERY_PAGE_SIZE
import com.zaen.githubuser.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.zaen.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_search_github_users.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchGithubUsersFragment : Fragment(R.layout.fragment_search_github_users) {

    lateinit private var githubUsersViewModel: GithubUsersViewModel
    lateinit private var githubUsersInfoAdapter: GithubUsersAdapter

    private val TAG = "SearchGithubUsersFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        githubUsersViewModel = (activity as GithubUsersActivity).githubUsersViewModel

        setupRecycleView()
        setupUsernameListenerWithFetchData()
        observeAndUpdateListOfGithubUsers()

    }

    private fun observeAndUpdateListOfGithubUsers() {
        githubUsersViewModel.searchGithubUsers.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { githubUsersResponse ->
                        githubUsersInfoAdapter.differ.submitList(githubUsersResponse.items)
                        val totalPages = githubUsersResponse.total_count / QUERY_PAGE_SIZE + 2
                        isLastPage = githubUsersViewModel.searchGithubUsersPage == totalPages
                        if(isLastPage) {
                            rvGithubUsers.setPadding(0, 0, 0, 0)
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

    private fun setupUsernameListenerWithFetchData() {
        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        githubUsersViewModel.searchGithubUsers(editable.toString(), false)
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private var isLoading = false
    private var isLastPage = false
    private var isScolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScolling

            if(shouldPaginate) {
                githubUsersViewModel.searchGithubUsers(etSearch.text.toString(), true)
                isScolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScolling = true
            }
        }
    }

    private fun setupRecycleView() {
        githubUsersInfoAdapter = GithubUsersAdapter()
        rvGithubUsers.apply {
            adapter = githubUsersInfoAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchGithubUsersFragment.scrollListener)
        }
    }

}