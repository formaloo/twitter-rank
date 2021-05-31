package co.idearun.twitter.data.remote.cdp

import okhttp3.RequestBody

/**
 * Implementation of [CDPService] interface
 */
class CDPDataSource(private val service: CDPService) {
    fun createActivity(body: RequestBody?) = service.createActivity(body,1)

}