package co.idearun.twitter.feature.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import co.idearun.twitter.data.model.FollowingsResponse
import co.idearun.twitter.data.model.User
import co.idearun.twitter.data.repository.AppDispatchers
import co.idearun.twitter.data.repository.twitter.TwitterRepositoryImpl
import co.idearun.twitter.feature.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class TwitterViewModel(
    private val repository: TwitterRepositoryImpl,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    private var screen_name: String = ""
    private var userFollowers = true

    private val _userErr = MediatorLiveData<String>()
    val userErr: LiveData<String> = _userErr
    private val _tweeterScore = MediatorLiveData<String>()
    val tweeterScore: LiveData<String> = _tweeterScore
    private val _profile = MediatorLiveData<User>()
    val profile: LiveData<User> = _profile
    private val _isLoading = MediatorLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _openShare = MediatorLiveData<Boolean>()
    val openShare: LiveData<Boolean> = _openShare
    private val _activeBtn = MediatorLiveData<Boolean>()
    val activeBtn: LiveData<Boolean> = _activeBtn
    private val _followingData = MediatorLiveData<FollowingsResponse>()
    val followingData: LiveData<FollowingsResponse> = _followingData

    var sInUsernameEdtContent: String
        @Bindable get() {
            return ""
        }
        set(value) {
            screen_name = value

            _activeBtn.value = value.isNotEmpty()

        }

    fun getFollowings() = launch {
        _isLoading.value = true

        val result = async(dispatchers.io) { repository.getFollowings(screen_name) }.await()
        result.either(::handleFailure, ::handleFollowings)
    }

    private fun handleFollowings(res: FollowingsResponse) {
        _followingData.value = res
        if (userFollowers) {
            userFollowers = false

        }

    }

    fun getUser() = launch {
        val result = async(dispatchers.io) { repository.getTweeter(screen_name) }.await()
        result.either(::handleFailure, ::handleUser)
    }

    private fun handleUser(res: User) {
        res?.let {
            _profile.value = it
        }

    }


    // click on button
    fun usernameInserted() {
        _isLoading.value = true
        getUser()
    }

    fun initTweeter(it: User) {
        _profile.value = it
    }

    //
    fun initTweeterScore(it: String?) {
        _tweeterScore.value = it
    }

    fun initScreenName(screenName: String) {
        screen_name = screenName
    }

    fun hideLoading() {
        _isLoading.value = false

    }


    fun errorFind(it: JSONObject) {
        if (it.has("message")) {
            _userErr.value = it["message"].toString()

        }

        _isLoading.value = false

    }

    fun hidePopUp() {
        _openShare.value = false
    }

    fun openPopUp() {
        _openShare.value = true
    }

}