package co.idearun.twitter.feature

import co.idearun.twitter.data.model.User

interface UserListener {
    fun openUserTwitter(item: User)
    fun shareRank(item: User)
}