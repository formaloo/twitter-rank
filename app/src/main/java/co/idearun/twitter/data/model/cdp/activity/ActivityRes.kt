package co.idearun.twitter.data.model.cdp.activity

import java.io.Serializable

data class ActivityRes(
    var status: Int? = null,
    var data: ActivityData? = null
) : Serializable {
    companion object {
        fun empty() = ActivityRes(0, null)
    }

    fun toActivityRes() = ActivityRes(status, data)
}