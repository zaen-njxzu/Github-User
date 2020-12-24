package com.zaen.githubuser.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaen.githubuser.GithubUsersApplication
import com.zaen.githubuser.models.DetailGithubUserResponse
import com.zaen.githubuser.models.FollowUserResponse
import com.zaen.githubuser.models.SearchGithubUsersResponse
import com.zaen.githubuser.repository.GithubUsersRepository
import com.zaen.githubuser.util.FollowStates
import com.zaen.githubuser.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class GithubUsersViewModel(
    val app: Application,
    val githubUsersRepository: GithubUsersRepository
) : AndroidViewModel(app) {

    val searchGithubUsers: MutableLiveData<Resource<SearchGithubUsersResponse>> = MutableLiveData()
    var searchGithubUsersPage = 1
    var searchGithubUsersResponse: SearchGithubUsersResponse? = null

    val detailGithubUser: MutableLiveData<Resource<DetailGithubUserResponse>> = MutableLiveData()
    val followersGithubUser: MutableLiveData<Resource<FollowUserResponse>> = MutableLiveData()
    val followingsGithubUser: MutableLiveData<Resource<FollowUserResponse>> = MutableLiveData()

    fun getFollowersGithubUser(username: String) = viewModelScope.launch {
        followingsGithubUser.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = githubUsersRepository.getGithubUserFollowers(username)
                followingsGithubUser.postValue(handleCommonResponse(response))
            } else {
                followingsGithubUser.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> followingsGithubUser.postValue(Resource.Error("Network Failure"))
                else -> followingsGithubUser.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getFollowingsGithubUser(username: String) = viewModelScope.launch {
        followersGithubUser.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = githubUsersRepository.getGithubUserFollowing(username)
                followersGithubUser.postValue(handleCommonResponse(response))
            } else {
                followersGithubUser.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> followersGithubUser.postValue(Resource.Error("Network Failure"))
                else -> followersGithubUser.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getDetailGithubUserByUsername(username: String) = viewModelScope.launch {
        detailGithubUser.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = githubUsersRepository.getDetailGithubUserByUsername(username)
                detailGithubUser.postValue(handleCommonResponse(response))
            } else {
                detailGithubUser.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> detailGithubUser.postValue(Resource.Error("Network Failure"))
                else -> detailGithubUser.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun searchGithubUsers(searchQuery: String, isPagination: Boolean) = viewModelScope.launch {
        searchGithubUsers.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = githubUsersRepository.searchGithubUsers(searchQuery, searchGithubUsersPage)
                searchGithubUsers.postValue(handleSearchGithubUsersResponse(response, isPagination))
            } else {
                searchGithubUsers.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchGithubUsers.postValue(Resource.Error("Network Failure"))
                else -> searchGithubUsers.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun <T> handleCommonResponse(response: Response<T>) : Resource<T> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchGithubUsersResponse(response: Response<SearchGithubUsersResponse>, isPagination: Boolean) : Resource<SearchGithubUsersResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (isPagination == false) {
                    searchGithubUsersPage = 1
                    return Resource.Success(resultResponse)
                }
                else {
                    searchGithubUsersPage++
                    if (searchGithubUsersResponse == null) {
                        searchGithubUsersResponse = resultResponse
                    } else {
                        val oldArticles = searchGithubUsersResponse?.items
                        val newArticles = resultResponse.items
                        oldArticles?.addAll(newArticles)
                    }
                    return Resource.Success(searchGithubUsersResponse ?: resultResponse)
                }
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<GithubUsersApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}