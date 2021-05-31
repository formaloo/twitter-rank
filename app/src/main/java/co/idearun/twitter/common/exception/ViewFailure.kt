package co.idearun.twitter.common.exception


class ViewFailure {
    class responseError(msg: String?) : Failure.FeatureFailure(msg)
}
