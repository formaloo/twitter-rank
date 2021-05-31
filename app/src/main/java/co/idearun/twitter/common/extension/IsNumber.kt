package co.idearun.twitter.common.extension

object NumberExt {
    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }
}