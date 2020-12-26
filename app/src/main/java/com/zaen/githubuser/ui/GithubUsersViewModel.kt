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
import com.zaen.githubuser.models.UserDetailsResponse
import com.zaen.githubuser.models.FollowUserResponse
import com.zaen.githubuser.models.SearchUsersResponse
import com.zaen.githubuser.models.UserInfo
import com.zaen.githubuser.repository.UsersRepository
import com.zaen.githubuser.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class GithubUsersViewModel(
    val app: Application,
    val usersRepository: UsersRepository
) : AndroidViewModel(app) {

    val searchUsers: MutableLiveData<Resource<SearchUsersResponse>> = MutableLiveData()
    var searchUsersPage = 1
    var searchUsersResponse: SearchUsersResponse? = null

    val userDetails: MutableLiveData<Resource<UserDetailsResponse>> = MutableLiveData()
    val followersUserData: MutableLiveData<Resource<FollowUserResponse>> = MutableLiveData()
    val followingsUserData: MutableLiveData<Resource<FollowUserResponse>> = MutableLiveData()

    fun saveFavoriteUser(userInfo: UserInfo) = viewModelScope.launch {
        usersRepository.upsert(userInfo)
    }

    fun getFavoriteUsers() = usersRepository.getFavoriteUsers()

    fun deleteFavoriteUser(userInfo: UserInfo) = viewModelScope.launch {
        usersRepository.deleteFavoriteUser(userInfo)
    }

    fun getFollowersUserData(username: String) = viewModelScope.launch {
        followingsUserData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = usersRepository.getFollowersUserData(username)
                followingsUserData.postValue(handleCommonResponse(response))
            } else {
                followingsUserData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> followingsUserData.postValue(Resource.Error("Network Failure"))
                else -> followingsUserData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getFollowingsUserData(username: String) = viewModelScope.launch {
        followersUserData.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = usersRepository.getFollowingsUserData(username)
                followersUserData.postValue(handleCommonResponse(response))
            } else {
                followersUserData.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> followersUserData.postValue(Resource.Error("Network Failure"))
                else -> followersUserData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getUserDetails(username: String) = viewModelScope.launch {
        userDetails.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = usersRepository.getUserDetails(username)
                userDetails.postValue(handleCommonResponse(response))
            } else {
                userDetails.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> userDetails.postValue(Resource.Error("Network Failure"))
                else -> userDetails.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun searchUsers(usernameQuery: String, isPagination: Boolean) = viewModelScope.launch {
        if(!isPagination) {
            searchUsersPage = 1
            searchUsers.postValue(null)
            searchUsersResponse = null
        }

        searchUsers.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = usersRepository.searchUsers(usernameQuery, searchUsersPage)
                searchUsers.postValue(handleSearchUsersResponse(response, isPagination))
            } else {
                searchUsers.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchUsers.postValue(Resource.Error("Network Failure"))
                else -> searchUsers.postValue(Resource.Error("Conversion Error"))
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

    private fun handleSearchUsersResponse(response: Response<SearchUsersResponse>, isPagination: Boolean) : Resource<SearchUsersResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchUsersPage++
                if(searchUsersResponse == null) searchUsersResponse = resultResponse

                if (isPagination) {
                    val oldUsersInfo = searchUsersResponse?.users_info
                    val newUsersInfo = resultResponse.users_info
                    oldUsersInfo?.addAll(newUsersInfo)
                    return Resource.Success(searchUsersResponse ?: resultResponse)
                } else {
                    return Resource.Success(resultResponse)
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