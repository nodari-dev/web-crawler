package dto

class Regex {
    companion object Values {
        const val GROUP_INDEX: Int = 2
        val A_TAG = "<a\\s+(?:[^>]*?\\s+)?href=([\"\'])(http.+?)\\1".toRegex()

    }
}