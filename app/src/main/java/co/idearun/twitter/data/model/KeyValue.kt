package co.idearun.twitter.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KeyValue(
    @SerializedName("key")
    val key: String?,
    @SerializedName("value")
    val value: String?
) : Serializable