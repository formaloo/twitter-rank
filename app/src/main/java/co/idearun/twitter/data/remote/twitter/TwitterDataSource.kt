package co.idearun.twitter.data.remote.twitter

import co.idearun.twitter.common.Constants.FOLLOWING_SIZE

class TwitterDataSource(private val service: TwitterService) {
    fun getFollowings(screen_name: String) = service.getFollowings(screen_name,FOLLOWING_SIZE)
    fun getTweeter(screen_name: String) = service.getTweeter(screen_name)
}