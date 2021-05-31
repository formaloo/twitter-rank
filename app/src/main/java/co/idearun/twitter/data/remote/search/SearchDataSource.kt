package co.idearun.twitter.data.remote.search


/**
 * Implementation of [CDPService] interface
 */
class SearchDataSource(private val service: SearchService) {
    fun searchCustomer(email: String) = service.searchCustomer(email,1)

}